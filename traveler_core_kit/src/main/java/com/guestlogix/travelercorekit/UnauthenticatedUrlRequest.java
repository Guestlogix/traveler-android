package com.guestlogix.travelercorekit;

import java.net.URL;
import java.util.Map;

class UnauthenticatedUrlRequest extends UrlRequest {
    UnauthenticatedUrlRequest(Method method, URL url, Map<String, String> headers) {
        super(method, url, headers);
    }
}
