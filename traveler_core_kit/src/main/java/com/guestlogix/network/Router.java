package com.guestlogix.network;

import android.content.Context;

import java.util.Collections;
import java.util.Map;

public class Router {

    private static final String BASE_URL = "https://guest-api-dev-1.guestlogix.io";

    private static final String AUTH_TOKEN = "/auth/token";

    private static final String DEVICE_ID = "deviceId";
    private static final String OS_VERSION = "osVersion";
    private static final String LANGUAGE = "language";
    private static final String LOCALE = "locale";
    private static final String REGION = "region";
    private static final String APPLICATION_ID = "applicationId";
    private static final String X_API_KEY = "x-api-key";


    public static NetworkRequest authenticate(String apiKey, Context applicationContext) {

        Map<String, String> payload = Collections.emptyMap();
        payload.put(DEVICE_ID, "android_123");
        payload.put(OS_VERSION, "oreo");
        payload.put(LANGUAGE, "en");
        payload.put(LOCALE, "en_POSIX");
        payload.put(REGION, "US");
        payload.put(APPLICATION_ID, "555");

        Map<String, String> headers = Collections.emptyMap();
        headers.put(X_API_KEY, "testtesttesttesttest");


        return new NetworkRequest(NetworkRequest.Method.POST, BASE_URL + AUTH_TOKEN, payload, headers);

    }
}
