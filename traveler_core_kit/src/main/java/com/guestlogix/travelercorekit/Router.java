package com.guestlogix.travelercorekit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.tasks.NetworkTask.Request.Method;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.guestlogix.travelercorekit.utilities.UrlHelper.urlEncodeUTF8;


public class Router {
    private static final String BASE_URL = "https://guest-traveler-api-develop.guestlogix.io";

    public static UnauthenticatedUrlRequest authenticate(String apiKey, Context context) {
        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/auth/token")
                .headers(buildHeaders(context))
                .apiKey(apiKey)
                .build();
    }

    public static AuthenticatedUrlRequest searchFlight(Session session, FlightQuery query, Context context) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("flight-number", query.getNumber());
        queryParams.put("departure-date", DateHelper.formatDateToISO8601(query.getDate()));

        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("flight")
                .headers(buildHeaders(context))
                .params(queryParams)
                .apiKey(session.getApiKey())
                .build(session.getAuthToken().getValue());
    }

    public static AuthenticatedUrlRequest getCatalog(Session session, CatalogQuery catalogQuery, Context context) {
        List<String> flightIds = new ArrayList<>();

        if (null != catalogQuery) {
            for (Flight flight : catalogQuery.getFlights()) {
                flightIds.add(flight.getId());
            }
        }

        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("flight-ids", flightIds);

        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/catalog")
                .headers(buildHeaders(context))
                .params(queryParams)
                .apiKey(session.getApiKey())
                .build(session.getAuthToken().getValue());
    }

    public static AuthenticatedUrlRequest getCatalogItem(Session session, CatalogItem catalogItem, Context context) {
        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/product/" + catalogItem.getId())
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getAuthToken().getValue());
    }

    public static AuthenticatedUrlRequest productSchedule(Session session, BookingContext bookingContext, Context context) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("from", DateHelper.formatDateToISO8601(bookingContext.getSelectedDate()));
        queryParams.put("to", DateHelper.formatDateToISO8601(bookingContext.getSelectedDate()));

        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/product/" + bookingContext.getProduct().getId() + "/schedule")
                .headers(buildHeaders(context))
                .params(queryParams)
                .apiKey(session.getApiKey())
                .build(session.getAuthToken().getValue());
    }

    public static AuthenticatedUrlRequest productPass(Session session, BookingContext bookingContext, Context context) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("date", DateHelper.formatDateToISO8601(bookingContext.getSelectedDate()));

        if (bookingContext.requiresTime()) {
            queryParams.put("time-in-minutes", bookingContext.getSelectedTime().toString());
        }

        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/product/" + bookingContext.getProduct().getId() + "/pass")
                .headers(buildHeaders(context))
                .params(queryParams)
                .apiKey(session.getApiKey())
                .build(session.getAuthToken().getValue());
    }

    public static AuthenticatedUrlRequest options(Session session, CatalogItem item, Context context) {
        return RequestBuilder.Builder()
                .method(Method.GET)
                .path("/product/" + item.getId() + "/option")
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getAuthToken().getValue());
    }

    /**
     * Extracts the device information and adds it to the headers.
     *
     * @param context Device context to extract information from.
     * @return Map of headers containing device info.
     */
    private static Map<String, String> buildHeaders(@NonNull Context context) {
        Map<String, String> headers = new HashMap<>();

        String applicationId = context.getPackageName();
        Locale locale = getLocale(context);
        String langCode = locale.getLanguage();
        String region = locale.getCountry();
        String localeCode = locale.toString();
        String osVersion = Build.VERSION.RELEASE;
        @SuppressLint("HardwareIds") String androidId =
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

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

    private static Locale getLocale(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }

    private static class RequestBuilder {
        private Method method;
        private String path;
        private String url;
        private Map<String, ?> params;
        private JsonPayload payload = null;
        private String apiKey;
        private Map<String, String> headers;

        private RequestBuilder() {
        }

        private static RequestBuilder Builder() {
            return new RequestBuilder();
        }

        public RequestBuilder method(Method method) {
            this.method = method;
            return this;
        }

        public RequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        public RequestBuilder url(String url) {
            this.url = url;
            return this;
        }

        public RequestBuilder params(Map<String, ?> params) {
            this.params = params;
            return this;
        }

        public RequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public RequestBuilder payload(JsonPayload payload) {
            this.payload = payload;
            return this;
        }

        public RequestBuilder apiKey(String key) {
            this.apiKey = key;
            return this;
        }

        public AuthenticatedUrlRequest build(String token) {
            URL url;
            if (params == null) {
                url = createURL(this.url, path);
            } else {
                url = createURL(this.url, path, params);
            }

            if (payload == null) {
                return new AuthenticatedUrlRequest(method, url, apiKey, token, headers);
            }

            return new AuthenticatedUrlRequest(method, url, apiKey, token, headers) {
                @Override
                public JSONObject getJSONPayload() {
                    return payload.getJsonPayload();
                }
            };
        }

        public UnauthenticatedUrlRequest build() {
            URL url;
            if (params == null) {
                url = createURL(this.url, path);
            } else {
                url = createURL(this.url, path, params);
            }

            if (payload == null) {
                return new UnauthenticatedUrlRequest(method, url, apiKey, headers);
            }

            return new UnauthenticatedUrlRequest(method, url, apiKey, headers) {
                @Override
                public JSONObject getJSONPayload() {
                    return payload.getJsonPayload();
                }
            };
        }

        public static URL createURL(String url, String path, Map<?, ?> queryParams) {
            try {
                return new URL(url + path + "?" + urlEncodeUTF8(queryParams));
            } catch (MalformedURLException e) {
                TravelerLog.e("Bad URL: %s", url + path);
                return null;
            }
        }

        public static URL createURL(String url, String path) {
            return createURL(url, path, null);
        }

        interface JsonPayload {
            JSONObject getJsonPayload();
        }
    }
}