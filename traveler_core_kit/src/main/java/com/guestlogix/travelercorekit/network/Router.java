package com.guestlogix.travelercorekit.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.task.NetworkTask;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
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

        return new UnauthenticatedRequest(NetworkTask.Request.Method.GET, createURL("/auth/token"), apiKey, getDeviceInformation(context));
    }

    public static AuthenticatedRequest searchFlight(Session session, FlightQuery query, Context context) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("flight-number", query.getNumber());
        queryParams.put("departure-date", DateHelper.getDateTimeAsString(query.getDate()));

        return new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL("/flight", queryParams), session.getApiKey(), getDeviceInformation(context), session.getAuthToken().getValue());
    }

    public static AuthenticatedRequest getCatalog(Session session, CatalogQuery catalogQuery, Context context) {
        List<String> flightIds = new ArrayList<>();

        if (null != catalogQuery) {
            for (Flight flight : catalogQuery.getFlights()) {
                flightIds.add(flight.getId());
            }
        }

        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("flight-ids", flightIds);

        return new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL("/catalog", queryParams), session.getApiKey(), getDeviceInformation(context), session.getAuthToken().getValue());
    }

    public static AuthenticatedRequest getCatalogItem(Session session, CatalogItem catalogItem, Context context) {
        return new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL("/product/" + catalogItem.getId()), session.getApiKey(), getDeviceInformation(context), session.getAuthToken().getValue());
    }

    public static AuthenticatedRequest productSchedule(Session session, BookingContext bookingContext, Context context) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("from", DateHelper.getDateTimeAsString(bookingContext.getSelectedDate()));
        queryParams.put("to", DateHelper.getDateTimeAsString(bookingContext.getEndDateTime()));

        return new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL(String.format(Locale.CANADA, "/product/%s/schedule", bookingContext.getProduct().getId()), queryParams), session.getApiKey(), getDeviceInformation(context), session.getAuthToken().getValue());
    }

    public static AuthenticatedRequest productPass(Session session, BookingContext bookingContext, Context context) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("date", DateHelper.getDateTimeAsString(bookingContext.getSelectedDate()));

        if (bookingContext.getTimeRequired()) {
            queryParams.put("time-in-minutes", bookingContext.getSelectedTime().toString());
        }

        return new AuthenticatedRequest(NetworkTask.Request.Method.GET, createURL(String.format(Locale.CANADA, "/product/%s/pass", bookingContext.getProduct().getId()), queryParams), session.getApiKey(), getDeviceInformation(context), session.getAuthToken().getValue());
    }

    /**
     * Extracts the device information and adds it to the headers.
     *
     * @param context Device context to extract information from.
     * @return Map of headers containing device info.
     */
    private static Map<String, String> getDeviceInformation(@NonNull Context context) {
        Map<String, String> headers = new HashMap<>();

        String applicationId = context.getPackageName();
        Locale locale = getLocale(context);
        String langCode = locale.getLanguage();
        String region = locale.getCountry();
        String localeCode = locale.toString();
        String osVersion = Build.VERSION.RELEASE;
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("x-device-id", androidId);
        headers.put("x-os-version", osVersion);
        headers.put("x-language", langCode);
        headers.put("x-locale", localeCode);
        headers.put("x-region", region);
        headers.put("x-application-id", applicationId);
        headers.put("x-timezone", "UTC");
        return headers;
    }

    public static Locale getLocale(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }
}