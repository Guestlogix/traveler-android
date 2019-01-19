package com.guestlogix.travelercorekit.network;

import android.content.Context;
import android.util.Log;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.task.NetworkTask;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static com.guestlogix.travelercorekit.utilities.UrlHelper.urlEncodeUTF8;


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

    public static AuthenticatedRequest getCatalogItem(Session session, CatalogItem catalogItem) {
        return new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL("/product/" + catalogItem.getId()), session.getApiKey(), session.getAuthToken().getValue());
    }

    public static AuthenticatedRequest productSchedule(Session session, BookingContext bookingContext) {

        return new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL(String.format(Locale.CANADA,"/product/%s/schedule",bookingContext.getProduct().getId())), session.getApiKey(), session.getAuthToken().getValue());
    }
}