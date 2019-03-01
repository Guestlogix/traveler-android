package com.guestlogix.travelercorekit.network;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

class UnauthenticatedUrlRequest extends UrlRequest {

    UnauthenticatedUrlRequest(Method method, URL url, String apiKey, Map<String, String> headers) {
        this(method, url, apiKey, headers, null);
    }

    UnauthenticatedUrlRequest(Method method, URL url, String apiKey, Map<String, String> headers, JSONObject payload) {
        super(method, url);
        this.url = url;
        this.apiKey = apiKey;
        this.payload = payload;
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() {
        headers.put("x-api-key", this.apiKey);
        return headers;
    }

}
