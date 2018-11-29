package com.guestlogix.network;

import android.content.Context;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Router {

    public static final String BASE_URL = "https://guest-api-dev-1.guestlogix.io";

    public static final String END_POINT_AUTH_TOKEN = "/auth/token";

    public static final String DEVICE_ID = "deviceId";
    public static final String OS_VERSION = "osVersion";
    public static final String LANGUAGE = "language";
    public static final String LOCALE = "locale";
    public static final String REGION = "region";
    public static final String APPLICATION_ID = "applicationId";
    public static final String X_API_KEY = "x-api-key";
    public static final String CONTENT_TYPE = "Content-Type";


    public static NetworkRequest authenticate(String apiKey, Context applicationContext) throws MalformedURLException {

        Map<String, String> headers = new HashMap<>();
        headers.put(X_API_KEY, "testtesttesttesttest");

        Map<String, String> payload = new HashMap<>();
        payload.put(DEVICE_ID, "android_123");
        payload.put(OS_VERSION, "oreo");
        payload.put(LANGUAGE, "en");
        payload.put(LOCALE, "en_POSIX");
        payload.put(REGION, "US");
        payload.put(APPLICATION_ID, "555");

        return new NetworkRequest(NetworkRequest.Method.POST, new URL(BASE_URL + END_POINT_AUTH_TOKEN), payload, headers);


    }
}
