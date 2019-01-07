package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
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
        try (JsonReader reader = new JsonReader(new InputStreamReader(stream))) {
            T model = mMappingFactory.instantiate(reader);

            mCallback.onSuccess(model);
        } catch (IOException e) {
            mCallback.onError(new Error());
        } catch (com.guestlogix.travelercorekit.network.MappingException e) {
            e.printStackTrace();
        }
    }
}
