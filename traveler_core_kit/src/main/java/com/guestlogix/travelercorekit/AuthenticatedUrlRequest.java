package com.guestlogix.travelercorekit;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

public class AuthenticatedUrlRequest extends UrlRequest {

    AuthenticatedUrlRequest(Method method, URL URL, String apiKey, String token, Map<String, String> headers) {
        super(method, URL);

        this.mToken = token;
        this.apiKey = apiKey;
        this.headers = headers;
    }

    AuthenticatedUrlRequest(Method method, URL url, String apiKey, Map<String, String> headers, String token) {
        this(method, url, apiKey, token, headers);
    }

    @Override
    public Map<String, String> getHeaders() {
        headers.put("x-api-key", this.apiKey);
        headers.put("Authorization", String.format("Bearer %s", this.mToken));

        return headers;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

}