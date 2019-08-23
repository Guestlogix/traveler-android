package com.guestlogix.travelercorekit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.tasks.NetworkTask.Request.Method;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.guestlogix.travelercorekit.utilities.UrlHelper.urlEncodeUTF8;


public class Router {
    private static final String BASE_URL = "https://traveler.rc.guestlogix.io/v1";

    public static UnauthenticatedUrlRequest authenticate(String apiKey, Context context) {
        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/auth/token")
                .headers(buildHeaders(context))
                .apiKey(apiKey)
                .build();
    }

    // /flight?flight-number=xxx&departure-date=xxx
    public static AuthenticatedUrlRequest searchFlight(Session session, FlightQuery query, Context context) {
        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/flight")
                .param("flight-number", query.getNumber())
                .param("departure-date", DateHelper.formatDateToISO8601(query.getDate()))
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getToken().getValue());
    }

    // /catalog?flight-ids=xxx&flight-ids=yyy...
    public static AuthenticatedUrlRequest catalog(Session session, CatalogQuery catalogQuery, Context context) {
        List<String> flightIds = new ArrayList<>();

        if (null != catalogQuery) {
            for (Flight flight : catalogQuery.getFlights()) {
                flightIds.add(flight.getId());
            }
        }

        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/catalog")
                .paramArray("flight-ids", flightIds) // catalogQuery.getFlights().stream().map(item -> item.getId()).collect(Collectors.toList());
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getToken().getValue());
    }

    // /product/{id}
    public static AuthenticatedUrlRequest product(Session session, Product product, Context context) {
        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/product/" + product.getId())
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getToken().getValue());
    }

    // /product/{id}/schedule
    public static AuthenticatedUrlRequest productSchedule(Session session, Product product, Date from, Date to, Context context) {
        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/product/" + product.getId() + "/schedule")
                .param("from", DateHelper.formatDateToISO8601(from))
                .param("to", DateHelper.formatDateToISO8601(to))
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getToken().getValue());
    }

    // /product/{id}/pass
    public static AuthenticatedUrlRequest productPass(Session session, Product product, Availability availability, @Nullable BookingOption option, Context context) {
        RequestBuilder rb = RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/product/" + product.getId() + "/pass")
                .param("availability-id", availability.getId())
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey());

        if (option != null) {
            rb.param("option-id", option.getId());
        } else {
            rb.param("option-id", availability.getId());
        }

        return rb.build(session.getToken().getValue());
    }

    // /product/{id}/question?&passes=xxx&passes=yyy&passes=zzz...
    public static AuthenticatedUrlRequest productQuestion(Session session, Product product, List<Pass> passes, Context context) {
        List<String> passIds = new ArrayList<>();

        for (Pass p : passes) {
            passIds.add(p.getId());
        }

        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/product/" + product.getId() + "/question")
                .paramArray("pass-ids", passIds)
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getToken().getValue());
    }

    public static AuthenticatedUrlRequest orderCreate(Session session, List<BookingForm> forms, Context context) {
        return RequestBuilder.Builder()
                .method(Method.POST)
                .url(BASE_URL)
                .path("/order")
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .payload(() -> {
                    try {
                        JSONObject payload = new JSONObject();
                        JSONArray products = new JSONArray();

                        for (BookingForm form : forms) {
                            JSONObject product = new JSONObject();
                            JSONArray passes = new JSONArray();
                            JSONArray answers = new JSONArray();

                            for (Pass p : form.getPasses()) {
                                passes.put(p.getId());
                            }

                            for (Answer a : form.getAnswers()) {
                                JSONObject answer = new JSONObject();
                                answer.put("questionId", a.getQuestionId());
                                answer.put("value", a.getCodedValue());

                                answers.put(answer);
                            }

                            product.put("id", form.getProduct().getId());
                            product.put("passes", passes);
                            product.put("answers", answers);

                            products.put(product);
                        }

                        payload.put("products", products);

                        // TODO don't send hardcoded value
                        JSONObject amount = new JSONObject();
                        amount.put("value", 0);
                        amount.put("currency", "USD");
                        payload.put("amount", amount);

                        payload.put("travelerId", session.getIdentity());

                        return payload;
                    } catch (JSONException e) {
                        TravelerLog.e("Router.orderCreate() could not create JSONPayloadProvider");
                    }

                    return null;
                })
                .build(session.getToken().getValue());
    }

    public static AuthenticatedUrlRequest orderProcess(Session session, Order order, Payment payment, Context context) {
        return RequestBuilder.Builder()
                .method(Method.PATCH)
                .url(BASE_URL)
                .path("/order/" + order.getId())
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .payload(payment::getSecurePayload)
                .build(session.getToken().getValue());
    }

    public static AuthenticatedUrlRequest orders(OrderQuery query, Session session, Context context) {
        RequestBuilder rb = RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/order")
                .param("traveler", session.getIdentity())
                .param("skip", String.valueOf(query.getOffset()))
                .param("take", String.valueOf(query.getLimit()))
                .param("to", DateHelper.formatDateToISO8601(query.getToDate()));

        if (query.getFromDate() != null) {
            rb.param("from", DateHelper.formatDateToISO8601(query.getFromDate()));
        }

        rb.headers(buildHeaders(context))
                .apiKey(session.getApiKey());

        return rb.build(session.getToken().getValue());
    }

    public static AuthenticatedUrlRequest cancellationQuote(Order order, Session session, Context context) {
        return RequestBuilder.Builder()
                .method(Method.GET)
                .url(BASE_URL)
                .path("/order/" + order.getId() + "/cancellation")
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getToken().getValue());
    }

    public static AuthenticatedUrlRequest cancelOrder(CancellationQuote quote, Session session, Context context) {
        return RequestBuilder.Builder()
                .method(Method.PATCH)
                .url(BASE_URL)
                .path("/order/" + quote.getOrder().getId() + "/cancellation/" + quote.getId())
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getToken().getValue());
    }

    public static AuthenticatedUrlRequest emailOrderConfirmation(Order order, Session session, Context context) {
        // TODO: DRY out the headers and api key and token stuff
        return RequestBuilder.Builder()
                .method(Method.PATCH)
                .url(BASE_URL)
                .path("/order/" + order.getId() + "/ticket")
                .headers(buildHeaders(context))
                .apiKey(session.getApiKey())
                .build(session.getToken().getValue());
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
        private JSONPayloadProvider payload = null;
        private String apiKey;
        private Map<String, String> headers;
        private List<String> params;

        private RequestBuilder() {
        }

        private static RequestBuilder Builder() {
            return new RequestBuilder();
        }

        static URL createURL(String url, String path, List<String> queryParams) {
            try {
                StringBuilder sb = new StringBuilder();

                sb.append(url);
                sb.append(path);

                if (queryParams != null) {
                    sb.append("?");

                    Iterator<String> paramIterator = queryParams.listIterator();

                    while (paramIterator.hasNext()) {
                        sb.append(paramIterator.next());

                        if (paramIterator.hasNext()) {
                            sb.append("&");
                        }
                    }
                }

                return new URL(sb.toString());
            } catch (MalformedURLException e) {
                TravelerLog.e("Bad URL: %s", url + path);
                return null;
            }
        }

        static URL createURL(String url, String path) {
            return createURL(url, path, null);
        }

        RequestBuilder method(Method method) {
            this.method = method;
            return this;
        }

        RequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        RequestBuilder url(String url) {
            this.url = url;
            return this;
        }

        RequestBuilder param(String key, String value) {
            if (params == null) {
                params = new ArrayList<>();
            }

            params.add(String.format("%s=%s", urlEncodeUTF8(key), urlEncodeUTF8(value)));
            return this;
        }

        public RequestBuilder payload(JSONPayloadProvider payload) {
            this.payload = payload;
            return this;
        }

        RequestBuilder paramArray(String key, List<String> values) {
            if (params == null) {
                params = new ArrayList<>();
            }

            for (String value : values) {
                params.add(String.format("%s=%s", urlEncodeUTF8(key), urlEncodeUTF8(value)));
            }
            return this;
        }

        RequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        RequestBuilder apiKey(String key) {
            this.apiKey = key;
            return this;
        }

        AuthenticatedUrlRequest build(String token) {
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

        UnauthenticatedUrlRequest build() {
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

        interface JSONPayloadProvider {
            JSONObject getJsonPayload();
        }
    }
}
