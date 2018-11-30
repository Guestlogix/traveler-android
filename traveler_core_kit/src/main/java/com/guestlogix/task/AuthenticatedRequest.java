package com.guestlogix.task;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

public class AuthenticatedRequest implements NetworkTask.Request {

    private Method mMethod;
    private URL mURL;
    private JSONObject mPayload;
    private String token;

    public AuthenticatedRequest(Method mMethod, URL mURL, String token, JSONObject mPayload) {
        this.mMethod = mMethod;
        this.mURL = mURL;
        this.token = token;
        this.mPayload = mPayload;
    }

    public AuthenticatedRequest(Method method, URL url, String token) {
        this(method, url, token, null);
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
    public HashMap<String, String> getHeaders() {

        HashMap<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
//        headers.put("x-api-key", this.token);

        return headers;

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
