package com.guestlogix.travelercorekit;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.models.Answer;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.BookingItemCategory;
import com.guestlogix.travelercorekit.models.BookingItemQuery;
import com.guestlogix.travelercorekit.models.BookingOption;
import com.guestlogix.travelercorekit.models.BoundingBox;
import com.guestlogix.travelercorekit.models.CancellationError;
import com.guestlogix.travelercorekit.models.CancellationQuote;
import com.guestlogix.travelercorekit.models.CancellationReason;
import com.guestlogix.travelercorekit.models.CancellationRequest;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Coordinate;
import com.guestlogix.travelercorekit.models.Currency;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.models.ItineraryQuery;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.travelercorekit.models.ParkingItemQuery;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.travelercorekit.models.PaymentError;
import com.guestlogix.travelercorekit.models.PriceRangeFilter;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.ProductType;
import com.guestlogix.travelercorekit.models.PurchaseError;
import com.guestlogix.travelercorekit.models.PurchaseForm;
import com.guestlogix.travelercorekit.models.PurchasePass;
import com.guestlogix.travelercorekit.models.PurchasedProductQuery;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.models.Token;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.models.WishlistQuery;
import com.guestlogix.travelercorekit.tasks.NetworkTask;
import com.guestlogix.travelercorekit.tasks.NetworkTaskError;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.TravelerPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.guestlogix.travelercorekit.utilities.TravelerPrefs.Key.TRAVELER_SDK_ENDPOINT;

public class Router {
    private static final String TAG = "Router";
    public static final String DEFAULT_ENDPOINT = "https://traveler.guestlogix.io/v1";

    public static UnauthenticatedUrlRequest authenticate(String apiKey, Context context) {
        return new RouteBuilder(context, apiKey)
                .method(NetworkTask.Route.Method.GET)
                .path("/auth/token")
                .build();
    }

    // /traveler/{travelerId}
    public static AuthenticatedUrlRequest storeAttributes(Session session, Map<String, Object> attributes, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.PUT)
                .path("/traveler/" + session.getIdentity())
                .payload(() -> {
                    try {
                        JSONObject payload = new JSONObject();

                        for (String key : attributes.keySet()) {
                            payload.put(key, attributes.get(key));
                        }

                        return payload;
                    } catch (JSONException e) {
                        Log.e(TAG, "Router.storeAttributes() could not create JSONPayloadProvider");
                    }

                    return null;
                });

        return routeBuilder.build(session.getToken());
    }

    // /traveler/{id}/itinerary
    public static AuthenticatedUrlRequest fetchItinerary(Session session, ItineraryQuery itineraryQuery, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/traveler/" + session.getIdentity() + "/itinerary");

        if (itineraryQuery.getStartDate() != null) {
            routeBuilder.param("from", DateHelper.formatDate(itineraryQuery.getStartDate()));
        }
        if (itineraryQuery.getEndDate() != null) {
            routeBuilder.param("to", DateHelper.formatDate(itineraryQuery.getEndDate()));
        }

        if (itineraryQuery.getFlights() != null) {
            for (Flight flight : itineraryQuery.getFlights()) {
                routeBuilder.param("flight-ids", flight.getId());
            }
        }

        return routeBuilder.build(session.getToken());
    }

    // /orderItemDetail/{OrderId}/booking/{ProductId})
    public static AuthenticatedUrlRequest fetchPurchasedBookingProductDetails(Session session, PurchasedProductQuery purchasedProductQuery, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/orderItemDetail/" + purchasedProductQuery.getOrderId() + "/booking/" + purchasedProductQuery.getProductId());

        return routeBuilder.build(session.getToken());
    }

    // /orderItemDetail/{OrderId}/booking/{ProductId})
    public static AuthenticatedUrlRequest fetchPurchasedParkingProductDetails(Session session, PurchasedProductQuery purchasedProductQuery, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/orderItemDetail/" + purchasedProductQuery.getOrderId() + "/parking/" + purchasedProductQuery.getProductId());

        return routeBuilder.build(session.getToken());
    }

    // /orderItemDetail/{OrderId}/booking/{ProductId})
    public static AuthenticatedUrlRequest fetchPurchasedPartnerOfferingProductDetails(Session session, PurchasedProductQuery purchasedProductQuery, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/orderItemDetail/" + purchasedProductQuery.getOrderId() + "/menu/" + purchasedProductQuery.getProductId());

        return routeBuilder.build(session.getToken());
    }

    // /orderItemDetail/{OrderId}/booking/{ProductId})
    public static AuthenticatedUrlRequest fetchPurchasedTransportationProductDetails(Session session, PurchasedProductQuery purchasedProductQuery, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/orderItemDetail/" + purchasedProductQuery.getOrderId() + "/transportation/" + purchasedProductQuery.getProductId());

        return routeBuilder.build(session.getToken());
    }

    // /menu/{id}/offerings
    public static AuthenticatedUrlRequest fetchPartnerOfferings(Session session, String partnerOfferingId, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/menu/" + partnerOfferingId + "/offerings")
                .build(session.getToken());
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

    // /catalog-group?flight-ids=xxx&flight-ids=yyy...
    public static AuthenticatedUrlRequest catalog(Session session, CatalogQuery catalogQuery, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/catalog-group");

        if (catalogQuery.getFlights() != null) {
            for (Flight flight : catalogQuery.getFlights()) {
                routeBuilder.param("flight-ids", flight.getId());
            }
        }

        if (catalogQuery.getProducts() != null) {
            for (Product product : catalogQuery.getProducts()) {
                routeBuilder.param("product-ids", product.getId());
            }
        }

        if (catalogQuery.getCity() != null && !catalogQuery.getCity().isEmpty()) {
            routeBuilder.param("city", catalogQuery.getCity());
        }

        if (catalogQuery.getLocation() != null) {
            routeBuilder.param("latitude", String.valueOf(catalogQuery.getLocation().getLatitude()));
            routeBuilder.param("longitude", String.valueOf(catalogQuery.getLocation().getLongitude()));
        }

        return routeBuilder.build(session.getToken());
    }

    // /booking
    public static AuthenticatedUrlRequest searchBookingItems(Session session, BookingItemQuery bookingItemQuery, Context context) {
        RouteBuilder rb = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/booking/")
                .param("text", bookingItemQuery.getQueryText() == null ? "" : bookingItemQuery.getQueryText())
                .param("skip", String.valueOf(bookingItemQuery.getOffset()))
                .param("take", String.valueOf(bookingItemQuery.getLimit()))
                .param("city", bookingItemQuery.getCity() == null ? "" : bookingItemQuery.getCity());


        if (bookingItemQuery.getCategories() != null)
            for (BookingItemCategory category : bookingItemQuery.getCategories()) {
                rb.param("sub_categories", category.getId());
            }

        if (bookingItemQuery.getPriceRangeFilter() != null) {
            PriceRangeFilter priceRangeFilter = bookingItemQuery.getPriceRangeFilter();
            rb.param("min-price", String.valueOf(priceRangeFilter.getRange().getLower()));
            rb.param("max-price", String.valueOf(priceRangeFilter.getRange().getUpper()));
            rb.param("currency", Currency.getCode(priceRangeFilter.getCurrency()));
        }

        if (bookingItemQuery.getBoundingBox() != null) {
            BoundingBox boundingBox = bookingItemQuery.getBoundingBox();
            Coordinate topLeftCoordinate = boundingBox.getTopLeftCoordinate();
            Coordinate bottomRightCoordinate = boundingBox.getBottomRightCoordinate();
            rb.param("top-left-latitude", String.valueOf(topLeftCoordinate.getLatitude()));
            rb.param("top-left-longitude", String.valueOf(topLeftCoordinate.getLongitude()));
            rb.param("bottom-right-latitude", String.valueOf(bottomRightCoordinate.getLatitude()));
            rb.param("bottom-right-longitude", String.valueOf(bottomRightCoordinate.getLongitude()));
        }

        if (bookingItemQuery.getBookingItemSort() != null) {
            rb.param("sort-field", bookingItemQuery.getBookingItemSort().getSortField().toString());
            rb.param("sort-order", bookingItemQuery.getBookingItemSort().getSortOrder().getSortValue());
        }

        if (bookingItemQuery.getLocation() != null) {
            rb.param("latitude", String.valueOf(bookingItemQuery.getLocation().getLatitude()));
            rb.param("longitude", String.valueOf(bookingItemQuery.getLocation().getLongitude()));
        }

        if (bookingItemQuery.getDateRangeFilter() != null && bookingItemQuery.getDateRangeFilter().getDateRange() != null) {
            Date startDate = bookingItemQuery.getDateRangeFilter().getDateRange().getLower();
            Date endDate = bookingItemQuery.getDateRangeFilter().getDateRange().getUpper();
            if (startDate != null) {
                rb.param("availability-start", DateHelper.formatDateToISO8601(startDate));
            }
            if (endDate != null) {
                rb.param("availability-end", DateHelper.formatDateToISO8601(endDate));
            }
        }

        return rb.build(session.getToken());
    }

    // /booking/{id}
    public static AuthenticatedUrlRequest bookingItem(Session session, Product product, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/booking/" + product.getId());

        if (!TextUtils.isEmpty(session.getIdentity()))
            routeBuilder.param("travelerId", session.getIdentity());

        return routeBuilder.build(session.getToken());
    }

    // /parking
    public static AuthenticatedUrlRequest searchParkingItems(Session session, ParkingItemQuery parkingItemQuery, Context context) {
        RouteBuilder rb = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/parking/")
                .param("to", DateHelper.formatDateToISO8601(parkingItemQuery.getDateRange().getUpper()))
                .param("from", DateHelper.formatDateToISO8601(parkingItemQuery.getDateRange().getLower()))
                .param("skip", String.valueOf(parkingItemQuery.getOffset()))
                .param("take", String.valueOf(parkingItemQuery.getLimit()));

        if (!TextUtils.isEmpty(parkingItemQuery.getAirportIATA())) {
            rb.param("airport", parkingItemQuery.getAirportIATA());
        }

        if (parkingItemQuery.getBoundingBox() != null) {
            BoundingBox boundingBox = parkingItemQuery.getBoundingBox();
            Coordinate topLeftCoordinate = boundingBox.getTopLeftCoordinate();
            Coordinate bottomRightCoordinate = boundingBox.getBottomRightCoordinate();
            rb.param("top-left-latitude", String.valueOf(topLeftCoordinate.getLatitude()));
            rb.param("top-left-longitude", String.valueOf(topLeftCoordinate.getLongitude()));
            rb.param("bottom-right-latitude", String.valueOf(bottomRightCoordinate.getLatitude()));
            rb.param("bottom-right-longitude", String.valueOf(bottomRightCoordinate.getLongitude()));
        }

        return rb.build(session.getToken());
    }

    // /parking/{id}
    public static AuthenticatedUrlRequest parkingItem(Session session, Product product, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/parking/" + product.getId());

        if (!TextUtils.isEmpty(session.getIdentity()))
            routeBuilder.param("travelerId", session.getIdentity());

        return routeBuilder.build(session.getToken());
    }

    // /menu/{id}
    public static AuthenticatedUrlRequest partnerOfferingItem(Session session, Product product, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/menu/" + product.getId());

        if (!TextUtils.isEmpty(session.getIdentity()))
            routeBuilder.param("travelerId", session.getIdentity());

        return routeBuilder.build(session.getToken());
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

    // /booking/{id}/question?&passes=xxx&passes=yyy&passes=zzz...
    public static AuthenticatedUrlRequest bookingQuestions(Session session, Product product, List<Pass> passes, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/booking/" + product.getId() + "/question");

        for (Pass pass : passes) {
            routeBuilder.param("pass-ids", pass.getId());
        }

        if (!TextUtils.isEmpty(session.getIdentity()))
            routeBuilder.param("travelerId", session.getIdentity());

        return routeBuilder.build(session.getToken());
    }

    // /product/{id}/question?&passes=xxx&passes=yyy&passes=zzz...
    public static AuthenticatedUrlRequest parkingQuestions(Session session, Product product, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/parking/" + product.getId() + "/question");
        return routeBuilder.build(session.getToken());
    }

    // /menu/{id}/question
    public static AuthenticatedUrlRequest partnerOfferingQuestions(Session session, Product product, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/menu/" + product.getId() + "/questions");
        return routeBuilder.build(session.getToken());
    }

    public static AuthenticatedUrlRequest createOrder(Session session, List<PurchaseForm> forms, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.POST)
                .path("/order")
                .payload(() -> {
                    try {
                        JSONObject payload = new JSONObject();
                        JSONArray products = new JSONArray();

                        for (PurchaseForm form : forms) {
                            JSONObject product = new JSONObject();
                            JSONArray passes = new JSONArray();
                            JSONArray answers = new JSONArray();

                            if (form.getProductOfferings() != null) {
                                for (PurchasePass p : form.getProductOfferings()) {
                                    passes.put(p.getId());
                                }
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
                        Log.e(TAG, "Router.createOrder() could not create JSONPayloadProvider");
                    }

                    return null;
                })
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest orderProcess(Session session, Order order, Payment payment, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.PATCH)
                .path("/order/" + order.getId())
                .payload(() -> {
                    try {
                        JSONObject payload = new JSONObject(payment.getSecurePayload().toString());
                        if (order.getDiscount() != null) {
                            payload.put("discountToken", order.getDiscount().getDiscountToken());
                        }
                        return payload;
                    } catch (JSONException e) {
                        Log.e(TAG, "Router.orderProcess() could not create JSONPayloadProvider");
                    }
                    return null;
                })
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest orders(OrderQuery query, Session session, Context context) {
        RouteBuilder rb = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/traveler/" + session.getIdentity() + "/order")
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

    public static AuthenticatedUrlRequest cancelOrder(CancellationRequest cancellationRequest, Session session, Context context) {
        CancellationQuote quote = cancellationRequest.getCancellationQuote();
        CancellationReason reason = cancellationRequest.getCancellationReason();
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.PATCH)
                .path("/order/" + quote.getOrder().getId() + "/cancellation/" + quote.getId())
                .payload(new RouteBuilder.JSONPayloadProvider() {
                    @Override
                    public JSONObject getJsonPayload() {
                        try {
                            JSONObject payload = new JSONObject();
                            payload.put("cancellationReason", reason.getId());
                            payload.put("cancellationReasonText", cancellationRequest.getExplanation());
                            return payload;
                        } catch (JSONException e) {
                            Log.e(TAG, "Router.cancelOrder() could not create JSONPayloadProvider");
                        }

                        return null;
                    }
                }).build(session.getToken());
    }

    public static AuthenticatedUrlRequest emailOrderConfirmation(Order order, Session session, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.PATCH)
                .path("/order/" + order.getId() + "/ticket")
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest wishlistAdd(Product product, String travelerId, Session session, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.POST)
                .path("/traveler/" + travelerId + "/wishlist")
                .payload(() -> {
                    try {
                        JSONObject payload = new JSONObject();
                        payload.put("product_id", product.getId());
                        return payload;
                    } catch (JSONException e) {
                        Log.e(TAG, "Router.wishlistAdd() could not create JSONPayloadProvider");
                    }
                    return null;
                })
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest wishlistRemove(Product product, String travelerId, Session session, Context context) {
        return new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.DELETE)
                .path("/traveler/" + travelerId + "/wishlist/" + product.getId())
                .build(session.getToken());
    }

    public static AuthenticatedUrlRequest wishlist(WishlistQuery query, String travelerId, Session session, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/traveler/" + travelerId + "/wishlist")
                .param("skip", String.valueOf(query.getOffset()))
                .param("take", String.valueOf(query.getLimit()));
        if (null != (query.getToDate())) {
            routeBuilder.param("to", DateHelper.formatDateToISO8601(query.getToDate()));
        }
        if (null != (query.getFromDate())) {
            routeBuilder.param("from", DateHelper.formatDateToISO8601(query.getFromDate()));
        }

        return routeBuilder.build(session.getToken());
    }

    public static void clearSdkEndpoint() {
        RouteBuilder.travelerSDKEndpoint = null;
    }

    public static AuthenticatedUrlRequest stripeEphemeralKey(String version, String travelerId, Session session, Context context) {
        RouteBuilder routeBuilder = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/traveler/" + travelerId + "/stripeEphemeralKey")
                .param("api-version", version);

        return routeBuilder.build(session.getToken());
    }

    // /category
    public static AuthenticatedUrlRequest fetchBookingItemCategories(Session session, ProductType productType, Context context) {
        RouteBuilder rb = new RouteBuilder(context, session.getApiKey())
                .method(NetworkTask.Route.Method.GET)
                .path("/category/")
                .param("purchaseStrategy", productType.toString());

        return rb.build(session.getToken());
    }

    private static class RouteBuilder {
        private static String travelerSDKEndpoint = null;

        private NetworkTask.Route.Method method;
        private String path;
        private JSONPayloadProvider payload = null;
        private Map<String, String> headers;
        private List<Pair<String, String>> params;

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
            this.headers.put("x-sandbox-mode", String.valueOf(Traveler.isSandboxMode()));

            if (null == travelerSDKEndpoint) {
                TravelerPrefs travelerPrefs = TravelerPrefs.getInstance(context);
                travelerSDKEndpoint = travelerPrefs.get(TRAVELER_SDK_ENDPOINT, DEFAULT_ENDPOINT);
            }
        }

        private URL getURL() {
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
                        sb.append(Uri.encode(param.second));

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
                public String getPayload() {
                    if (payload == null)
                        return null;

                    return payload.getJsonPayload().toString();
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
                public String getPayload() {
                    if (payload == null)
                        return null;

                    return payload.getJsonPayload().toString();
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
            JSONObjectGLX jsonObject = new JSONObjectGLX(networkTaskError.getMessage());
            int code = jsonObject.getInt("errorCode");
            String message = jsonObject.getNullableString("errorMessage");

            switch (code) {
                case 2006:
                    return new PurchaseError(PurchaseError.Code.PASSES_UNAVAILABLE, message);
                case 2007:
                    return new PurchaseError(PurchaseError.Code.VERY_OLD_TRAVELER, message);
                case 2012:
                case 2013:
                case 2014:
                    return new CancellationError(CancellationError.Code.NOT_CANCELLABLE);
                case 2015:
                    return new PurchaseError(PurchaseError.Code.ADULT_AGE_INVALID, message);
                case 2017:
                    return new PurchaseError(PurchaseError.Code.BELOW_MIN_UNITS, message);
                case 2018:
                    return new PurchaseError(PurchaseError.Code.UNACCOMPANIED_CHILDREN, message);
                case 2032:
                    return new NetworkTaskError(NetworkTaskError.Code.ITEM_UNAVAILABLE);
                case 2027:
                    return new PaymentError(PaymentError.Code.CONFIRMATION_REQUIRED,
                            jsonObject.getJSONObject("errorData")
                                    .getString("confirmationKey"));
                case 6001:
                    return new PaymentError(PaymentError.Code.PROCESSING_ERROR, null);
                case 8001:
                    return new NetworkTaskError(NetworkTaskError.Code.ALREADY_WISHLISTED);
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
