package com.guestlogix.travelercorekit.network;

import android.content.Context;
import android.util.Log;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.task.NetworkTask;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static UnauthenticatedRequest authenticate(String apiKey, Context context) {

        Map<String, String> payload = new HashMap<>();
        payload.put("deviceId", "android_678");
        payload.put("osVersion", "oreo");
        payload.put("language", "en");
        payload.put("locale", "en_POSIX");
        payload.put("region", "US");
        payload.put("applicationId", "555");

        return new UnauthenticatedRequest(NetworkTask.Request.Method.POST, createURL("/auth/token"), apiKey, new JSONObject(payload));
    }

    public static UnauthenticatedRequest downloadImage(URL imageUrl) {

        return new UnauthenticatedRequest(NetworkTask.Request.Method.GET, imageUrl, "", null);
    }

    public static AuthenticatedRequest searchFlight(Session session, FlightQuery query) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("flight-number", query.getNumber());
        queryParams.put("departure-date", DateHelper.getDateAsString(query.getDate()));

        return new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL("/flight", queryParams), session.getApiKey(), session.getAuthToken().getValue());
    }

    public static AuthenticatedRequest getCatalog(Session session, CatalogQuery catalogQuery) {

        List<String> flightIds = new ArrayList<>();

        if (null != catalogQuery) {
            for (Flight flight : catalogQuery.getFlights()) {
                flightIds.add(flight.getId());
            }
        }

        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("flight-ids", flightIds);

        return new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL("/catalog", queryParams), session.getApiKey(), session.getAuthToken().getValue());
    }

    private static String urlEncodeUTF8(Map<?, ?> map) {
        if (null == map)
            return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            if (entry.getValue() instanceof List) {
                String key = entry.getKey().toString();
                List values = (List) entry.getValue();
                sb.append(urlEncodeUTF8(values, key));
            } else {
                sb.append(String.format("%s=%s",
                        urlEncodeUTF8(entry.getKey().toString()),
                        urlEncodeUTF8(entry.getValue().toString())
                ));
            }
        }
        return sb.toString();
    }

    private static String urlEncodeUTF8(List values, String key) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0 ; i < values.size(); i++) {
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(key),
                    urlEncodeUTF8(values.get(i).toString())
            ));

            if (i < values.size() - 1) {
                sb.append('&');
            }
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