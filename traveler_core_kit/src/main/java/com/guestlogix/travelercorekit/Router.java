package com.guestlogix.travelercorekit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.tasks.NetworkTask;
import com.guestlogix.travelercorekit.tasks.NetworkTaskError;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class Router {
    private static final String DEFAULT_ENDPOINT = "https://traveler.rc.guestlogix.io/v1";
    private static String TAG = "Traveler";

    public static UnauthenticatedUrlRequest authenticate(String apiKey, Context context) {
        return new RouteBuilder(context, apiKey)
                .method(NetworkTask.Route.Method.GET)
                .path("/auth/token")
                .build();
    }

    // /flight?flight-number=xxx&departure-date=xxx
    public static AuthenticatedUrlRequest searchFlight(Session session, FlightQuery query, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/flight")
                .param("flight-number", query.getNumber())
                .param("departure-date", DateHelper.formatDateToISO8601(query.getDate()))
                .build(session.getToken());
    }

    // /catalog?flight-ids=xxx&flight-ids=yyy...
    public static AuthenticatedUrlRequest catalog(Session session, CatalogQuery catalogQuery, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/catalog");

        for (Flight flight : catalogQuery.getFlights()) {
            routeBuilder.param("flight-ids", flight.getId());
        }

        return routeBuilder
                .build(session.getToken());
    }

    // /product/{id}
    public static AuthenticatedUrlRequest product(Session session, Product product, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/product/" + product.getId())
                .build(session.getToken());
    }

    // /product/{id}/schedule
    public static AuthenticatedUrlRequest productSchedule(Session session, Product product, Date from, Date to, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/product/" + product.getId() + "/schedule")
                .param("from", DateHelper.formatDateToISO8601(from))
                .param("to", DateHelper.formatDateToISO8601(to))
                .build(session.getToken());
    }

    // /product/{id}/pass
    public static AuthenticatedUrlRequest productPass(Session session, Product product, Availability availability, @Nullable BookingOption option, Context context) {
        RouteBuilder rb = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/product/" + product.getId() + "/pass")
                .param("availability-id", availability.getId());

        if (option != null)
            rb.param("option-id", option.getId());

        return rb.build(session.getToken());
    }

    // /product/{id}/question?&passes=xxx&passes=yyy&passes=zzz...
    public static AuthenticatedUrlRequest productQuestion(Session session, Product product, List<Pass> passes, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/product/" + product.getId() + "/question");

        for (Pass pass : passes) {
            routeBuilder.param("pass-ids", pass.getId());
        }

        return routeBuilder
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest orderCreate(Session session, List<BookingForm> forms, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.POST)
                .path("/order")
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
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest orderProcess(Session session, Order order, Payment payment, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.PATCH)
                .path("/order/" + order.getId())
                .payload(new RouteBuilder.JSONPayloadProvider() {
                    @Override
                    public JSONObject getJsonPayload() {
                        return payment.getSecurePayload();
                    }
                })
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest orders(OrderQuery query, Session session, Context context) {
        RouteBuilder rb = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/order")
                .param("traveler", session.getIdentity())
                .param("skip", String.valueOf(query.getOffset()))
                .param("take", String.valueOf(query.getLimit()))
                .param("to", DateHelper.formatDateToISO8601(query.getToDate()));

        if (query.getFromDate() != null)
            rb.param("from", DateHelper.formatDateToISO8601(query.getFromDate()));

        return rb.build(session.getToken());
    }

    public static AuthenticatedUrlRequest cancellationQuote(Order order, Session session, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/order/" + order.getId() + "/cancellation")
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest cancelOrder(CancellationQuote quote, Session session, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.PATCH)
                .path("/order/" + quote.getOrder().getId() + "/cancellation/" + quote.getId())
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest emailOrderConfirmation(Order order, Session session, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.PATCH)
                .path("/order/" + order.getId() + "/ticket")
                .build(session.getToken());
    }

    private static class RouteBuilder {
        private NetworkTask.Route.Method method;
        private String path;
        private String url;
        private JSONPayloadProvider payload = null;
        private String apiKey;
        private Map<String, String> headers;
        private List<Pair<String, String>> params;
        private Context context;

        private RouteBuilder(Context context, String apiKey) {
            this.params = new ArrayList<>();

            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = context.getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = context.getResources().getConfiguration().locale;
            }

            String applicationId = context.getPackageName();
            String langCode = locale.getLanguage();
            String region = locale.getCountry();
            String localeCode = locale.toString();
            String osVersion = Build.VERSION.RELEASE;
            // TODO: Get better device ID, something compatible
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            this.headers = new HashMap<>();
            this.headers.put("Content-Type", "application/json");
            this.headers.put("Accept", "application/json");
            this.headers.put("x-device-id", androidId);
            this.headers.put("x-os-version", osVersion);
            this.headers.put("x-language", langCode);
            this.headers.put("x-locale", localeCode);
            this.headers.put("x-region", region);
            this.headers.put("x-application-id", applicationId);
            this.headers.put("x-timezone", "UTC");
            this.headers.put("x-api-key", apiKey);
            this.context = context;
        }

        private URL getURL() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("TRAVELER_PREFS", Context.MODE_PRIVATE);
            String travelerSDKEndpoint = sharedPreferences.getString("TRAVELER_SDK_ENDPOINT", DEFAULT_ENDPOINT);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(travelerSDKEndpoint);
                sb.append(path);

                if (!params.isEmpty()) {
                    sb.append("?");

                    Iterator<Pair<String, String>> iterator = params.iterator();

                    while (iterator.hasNext()) {
                        Pair<String, String> param = iterator.next();

                        sb.append(param.first);
                        sb.append("=");
                        sb.append(param.second);

                        if (iterator.hasNext())
                            sb.append("&");
                    }
                }

                return new URL(sb.toString());
            } catch (MalformedURLException e) {
                Log.e("RouteBuilder", "Bad URL: " + travelerSDKEndpoint + path);
                return null;
            }
        }

        RouteBuilder method(NetworkTask.Route.Method method) {
            this.method = method;
            return this;
        }

        RouteBuilder path(String path) {
            this.path = path;
            return this;
        }

        RouteBuilder param(String key, String value) {
            if (params == null) {
                params = new ArrayList<>();
            }

            params.add(new Pair<>(key, value));
            return this;
        }

        RouteBuilder payload(JSONPayloadProvider payload) {
            this.payload = payload;
            return this;
        }

        AuthenticatedUrlRequest build(Token token) {
            String tokenValue = (null != token) ? token.getValue() : null;
            return new AuthenticatedUrlRequest(method, getURL(), headers, tokenValue) {
                @Override
                public JSONObject getJSONPayload() {
                    if (payload == null)
                        return null;

                    return payload.getJsonPayload();
                }

                @Override
                public Error transformError(NetworkTaskError error) {
                    return Router.transformError(error);
                }
            };
        }

        UnauthenticatedUrlRequest build() {
            return new UnauthenticatedUrlRequest(method, getURL(), headers) {
                @Override
                public JSONObject getJSONPayload() {
                    if (payload == null)
                        return null;

                    return payload.getJsonPayload();
                }

                @Override
                public Error transformError(NetworkTaskError error) {
                    return Router.transformError(error);
                }
            };
        }

        interface JSONPayloadProvider {
            JSONObject getJsonPayload();
        }
    }

    private static Error transformError(Error error) {
        if (!(error instanceof NetworkTaskError))
            return error;

        NetworkTaskError networkTaskError = (NetworkTaskError) error;

        if (networkTaskError.getCode() != NetworkTaskError.Code.CLIENT_ERROR)
            return error;

        try {
            JSONObject json = new JSONObject(networkTaskError.getMessage());
            int code = json.getInt("errorCode");

            switch (code) {
                case 2006:
                    return new BookingError(BookingError.Code.PASSES_UNAVAILABLE);
                case 2007:
                    return new BookingError(BookingError.Code.VERY_OLD_TRAVELER);
                case 2012:
                case 2013:
                    return new CancellationError(CancellationError.Code.NOT_CANCELLABLE);
                case 2014:
                    return new BookingError(BookingError.Code.BELLOW_MIN_UNITS);
                case 2018:
                    return new BookingError(BookingError.Code.UNACCOMPANIED_CHILDREN);
                case 6001:
                    return new PaymentError(PaymentError.Code.PROCESSING_ERROR);
                default:
                    Log.e("ErrorMapping", "Unknown error code: " + code);
                    return error;
            }
        } catch (JSONException e) {
            Log.e("ErrorMapping", "Bad JSON from error response: \n" + networkTaskError.getMessage());
            return error;
        }
    }
}
