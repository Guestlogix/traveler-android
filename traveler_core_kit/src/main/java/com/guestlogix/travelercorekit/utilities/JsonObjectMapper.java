package com.guestlogix.travelercorekit.utilities;

import com.guestlogix.travelercorekit.network.MappingFactory;
import com.guestlogix.travelercorekit.task.NetworkTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonObjectMapper<T> implements NetworkTask.ResponseHandler {
    private MappingFactory<T> mMappingFactory;
    private Callback<T> mCallback;

    public interface Callback<T> {
        void onSuccess(T model);
        void onError(Error error);
    }

    public JsonObjectMapper(MappingFactory<T> mappingFactory, Callback<T> callback) {
        mMappingFactory = mappingFactory;
        mCallback = callback;
    }

    @Override
    public void onHandleResponse(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();

        // TODO: Rewrite this using JSONReader

        // TODO: handle error correctly

        try {
            String inputStr;
            while ((inputStr = reader.readLine()) != null)
                builder.append(inputStr);

            JSONObject jsonObject = new JSONObject(builder.toString());

            T model = mMappingFactory.instantiate(jsonObject);

            mCallback.onSuccess(model);
        } catch (IOException e) {
            mCallback.onError(new Error());
        } catch (JSONException e) {
            mCallback.onError(new Error());
        } catch (com.guestlogix.travelercorekit.network.MappingException e) {
            e.printStackTrace();
        }
    }
}
