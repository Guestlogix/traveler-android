package com.guestlogix.travelercorekit;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AuthenticatedUrlRequest extends UrlRequest {
    private String mToken;

    AuthenticatedUrlRequest(Method method, URL URL, Map<String, String> headers, String token) {
        super(method, URL, headers);

        this.mToken = token;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>(super.getHeaders());
        headers.put("Authorization", String.format("Bearer %s", mToken));
        return headers;
    }
}
