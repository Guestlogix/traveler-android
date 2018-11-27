package com.guestlogix.network;

import java.io.InputStream;


public class JsonObjectMapper<T> implements NetworkResponseHandler {

    JsonObjectMapperCallback<T> mCallback;

    public JsonObjectMapper(JsonObjectMapperCallback<T> mCallback) {
        this.mCallback = mCallback;
    }

    public void onSuccess(InputStream is) {
        // read JSON stream from input stream
        // if error in reading json ever occurs
        //    mCallback.onError(pass the error in here)

        // otherwise keep reading into a JSONObject
        // once you have JSONObject

        try {
            T model = T.instantiate(json);

        } catch (Exception e) {
            mCallback.onError(new Error(e.getMessage()));
        }

        mCallback.onSuccess(model);

    }

    public void onError(Error error) {
        mCallback.onError(error);

    }


}
