package com.guestlogix.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;

//Generic
public class JsonObjectMapper<T> implements NetworkResponseHandler {

    private JsonObjectMapperCallback<T> mCallback;
    private T var;
    private MappingFactory<T> fact;

    public JsonObjectMapper(JsonObjectMapperCallback<T> mCallback, MappingFactory<T> fact) {
        this.mCallback = mCallback;
        this.fact = fact;
    }

    public void onSuccess(InputStream is) {

        try {

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()));

            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            var = fact.instantiate(new JSONObject(responseStrBuilder.toString()));

            mCallback.onSuccess(var);

        } catch (UnsupportedEncodingException e) {
            mCallback.onError(new Error(e));
        } catch (JSONException e) {
            mCallback.onError(new Error(e));
        } catch (IOException e) {
            mCallback.onError(new Error(e));
        } catch (MappingException e) {
            e.printStackTrace();
        }

    }

    public void onError(Error error) {
        mCallback.onError(error);

    }

    public MappingFactory<T> getFact() {
        return fact;
    }

}
