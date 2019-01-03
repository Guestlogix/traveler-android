package com.guestlogix.travelercorekit.network;

import com.guestlogix.travelercorekit.task.NetworkTask;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

class UnauthenticatedRequest implements NetworkTask.Request {
    private Method mMethod;
    private URL mURL;
    private JSONObject mPayload;
    private String mApiKey;

    UnauthenticatedRequest(Method method, URL url, String apiKey) {
        this(method, url, apiKey, null);
    }

    UnauthenticatedRequest(Method method, URL url, String apiKey, JSONObject payload) {
        this.mMethod = method;
        this.mURL = url;
        this.mApiKey = apiKey;
        this.mPayload = payload;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("x-api-key", this.mApiKey);


        return headers;
    }

    @Override
    public Method getMethod() {
        return mMethod;
    }

    @Override
    public URL getURL() {
        return mURL;
    }

    @Override
    public void onProvidePayload(OutputStream stream) {
        if (mPayload == null) {
            return;
        }

        try {
            stream.write(mPayload.toString().getBytes());
        } catch (IOException e) {
            // TODO: Handle error
        }
    }
}
