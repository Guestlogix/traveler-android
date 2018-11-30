package com.guestlogix.task;

import android.content.Context;
import android.util.Log;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Router {
    static final String BASE_URL = "https://guest-api-dev-1.guestlogix.io";

    private static URL createURL(String path) {
        try {
            return new URL(BASE_URL + path);
        } catch (MalformedURLException e) {
            Log.e("Router", "Bad URL: " + BASE_URL + path);
            return null;
        }
    }

    public static NetworkTask.Request authenticate(String apiKey, Context context) {

        Map<String, String> payload = new HashMap<>();
        payload.put("deviceId", "android_123");
        payload.put("osVersion", "oreo");
        payload.put("language", "en");
        payload.put("locale", "en_POSIX");
        payload.put("region", "US");
        payload.put("applicationId", "555");

        UnauthenticatedRequest request = new UnauthenticatedRequest(NetworkTask.Request.Method.POST, createURL("/auth/token"), apiKey, new JSONObject(payload));

        return request;
    }
}
