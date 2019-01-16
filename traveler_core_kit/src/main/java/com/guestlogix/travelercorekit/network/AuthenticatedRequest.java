package com.guestlogix.travelercorekit.network;

import com.guestlogix.travelercorekit.task.NetworkTask;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

public class AuthenticatedRequest implements NetworkTask.Request {

    private Method mMethod;
    private URL mURL;
    private JSONObject mPayload;
    private String mToken;
    private String mApiKey;

    public AuthenticatedRequest(Method method, URL URL, String apiKey, String token, JSONObject payload) {
        this.mMethod = method;
        this.mURL = URL;
        this.mApiKey = apiKey;
        this.mToken = token;
        this.mPayload = payload;
    }

    public AuthenticatedRequest(Method method, URL url, String apiKey, String token) {
        this(method, url, apiKey, token, null);
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
        headers.put("x-api-key", this.mApiKey);
        headers.put("Authorization", String.format("Bearer %s", this.mToken));

        return headers;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
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
