package com.guestlogix.travelercorekit.network;

import org.json.JSONObject;

import java.net.URL;
import java.util.Map;

public class AuthenticatedRequest extends UnauthenticatedRequest {
    private String mToken;


    public AuthenticatedRequest(Method method, URL URL, String apiKey, String token, Map<String, String> headers, JSONObject payload) {
        super(method, URL, apiKey, headers, payload);
        this.mToken = token;
    }

    public AuthenticatedRequest(Method method, URL url, String apiKey, Map<String, String> headers, String token) {
        this(method, url, apiKey, token, headers, null);
    }


    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = super.getHeaders();
        headers.put("Authorization", String.format("Bearer %s", this.mToken));

        return headers;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }
}
