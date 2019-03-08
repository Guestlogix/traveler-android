package com.guestlogix.travelercorekit;

import java.net.URL;
import java.util.Map;

class UnauthenticatedUrlRequest extends UrlRequest {

    UnauthenticatedUrlRequest(Method method, URL url, String apiKey, Map<String, String> headers) {
        super(method, url);

        this.url = url;
        this.apiKey = apiKey;
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() {
        headers.put("x-api-key", this.apiKey);
        return headers;
    }

}
