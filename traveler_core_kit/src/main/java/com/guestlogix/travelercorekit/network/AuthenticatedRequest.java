package com.guestlogix.travelercorekit.network;

import com.guestlogix.travelercorekit.task.NetworkTask;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

public class AuthenticatedRequest implements NetworkTask.Request {
    private Method mMethod;
    private URL mURL;
    private JSONObject mPayload;
    private String mToken;
    private String mApiKey;
    private Map<String, String> headers;


    public AuthenticatedRequest(Method method, URL URL, String apiKey, String token, Map<String, String> headers, JSONObject payload) {
        this.mMethod = method;
        this.mURL = URL;
        this.mApiKey = apiKey;
        this.mToken = token;
        this.headers = headers;
        this.mPayload = payload;
    }

    public AuthenticatedRequest(Method method, URL url, String apiKey, Map<String, String> headers, String token) {
        this(method, url, apiKey, token, headers, null);
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
    public Map<String, String> getHeaders() {

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
