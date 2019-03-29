package com.guestlogix.travelercorekit;

import java.net.URL;
import java.util.Map;

class UnauthenticatedUrlRequest extends UrlRequest {

    private String apiKey;

    UnauthenticatedUrlRequest(Method method, URL url, String apiKey, Map<String, String> headers) {
        super(method, url, headers);
        this.apiKey = apiKey;
    }

    @Override
    public Map<String, String> getHeaders() {
        headers.put("x-api-key", this.apiKey);
        return headers;
    }

}
