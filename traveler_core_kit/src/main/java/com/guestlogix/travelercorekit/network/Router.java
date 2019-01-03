package com.guestlogix.travelercorekit.network;

import android.content.Context;
import android.util.Log;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.task.NetworkTask;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class Router {
    static final String BASE_URL = "https://guest-traveler-api-develop.guestlogix.io";

    private static URL createURL(String path, Map<?, ?> queryParams) {
        try {
            return new URL(BASE_URL + path + "?" + urlEncodeUTF8(queryParams));
        } catch (MalformedURLException e) {
            Log.e("Router", "Bad URL: " + BASE_URL + path);
            return null;
        }
    }

    private static URL createURL(String path) {
        return createURL(path, null);
    }

    public static NetworkTask.Request authenticate(String apiKey, Context context) {

        Map<String, String> payload = new HashMap<>();
        payload.put("deviceId", "android_678");
        payload.put("osVersion", "oreo");
        payload.put("language", "en");
        payload.put("locale", "en_POSIX");
        payload.put("region", "US");
        payload.put("applicationId", "555");

        UnauthenticatedRequest request = new UnauthenticatedRequest(NetworkTask.Request.Method.POST, createURL("/auth/token"), apiKey, new JSONObject(payload));

        return request;
    }

    public static NetworkTask.Request getCatalogue(Session session, String id) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("flight-ids", id);


        AuthenticatedRequest request = new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL("/catalog", queryParams), session.getAuthToken().getValue());

        return request;
    }

    private static String urlEncodeUTF8(Map<?, ?> map) {
        if (null == map)
            return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}