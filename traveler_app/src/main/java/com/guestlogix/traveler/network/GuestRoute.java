package com.guestlogix.traveler.network;

import android.content.Context;
import android.util.Log;

import com.guestlogix.travelercorekit.UrlRequest;
import com.guestlogix.travelercorekit.tasks.NetworkTask;
import com.guestlogix.travelercorekit.utilities.TravelerPrefs;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.guestlogix.travelercorekit.utilities.TravelerPrefs.Key.TRAVELER_AUTH_ENDPOINT;
import static com.guestlogix.travelercorekit.utilities.UrlHelper.urlEncodeUTF8;

public class GuestRoute {
    private static final String TAG = "GuestRoute";
    public static final String DEFAULT_AUTH_URL = "https://9th3dtgfg3.execute-api.ca-central-1.amazonaws.com/dev";

    private GuestRoute() {
        // prevent instantiation
    }

    static UrlRequest profile(String requestIdToken, Context context) {
        return new GuestRequestBuilder(context, requestIdToken)
                .method(NetworkTask.Route.Method.GET)
                .path("/login")
                .build();
    }

    private static class GuestRequestBuilder {
        private NetworkTask.Route.Method method;
        private String path;
        private String url;
        private GuestRequestBuilder.JSONPayloadProvider payload = null;
        private Map<String, String> headers;
        private List<String> params;
        private TravelerPrefs travelerPrefs;

        private GuestRequestBuilder(Context context, String requestIdToken) {
            this.params = new ArrayList<>();

            this.headers = new HashMap<>();
            this.headers.put("Content-Type", "application/json");
            this.headers.put("Accept", "application/json");
            this.headers.put("x-access-token", requestIdToken);
            this.travelerPrefs = TravelerPrefs.getInstance(context);
        }

        private URL createURL(String path, List<String> queryParams) {
            String travelerSDKEndpoint = travelerPrefs.get(TRAVELER_AUTH_ENDPOINT, DEFAULT_AUTH_URL);

            try {
                StringBuilder sb = new StringBuilder();
                sb.append(travelerSDKEndpoint);
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
                Log.e(TAG, String.format("Bad URL: %s", url + path));
                return null;
            }
        }

        private URL createURL(String path) {
            return createURL(path, null);
        }

        GuestRequestBuilder method(NetworkTask.Route.Method method) {
            this.method = method;
            return this;
        }

        GuestRequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        GuestRequestBuilder param(String key, String value) {
            if (params == null) {
                params = new ArrayList<>();
            }

            params.add(String.format("%s=%s", urlEncodeUTF8(key), urlEncodeUTF8(value)));
            return this;
        }

        GuestRequestBuilder payload(GuestRequestBuilder.JSONPayloadProvider payload) {
            this.payload = payload;
            return this;
        }

        GuestRequestBuilder paramArray(String key, List<String> values) {
            if (params == null) {
                params = new ArrayList<>();
            }

            for (String value : values) {
                params.add(String.format("%s=%s", urlEncodeUTF8(key), urlEncodeUTF8(value)));
            }
            return this;
        }

        UrlRequest build() {
            URL url;
            if (params == null) {
                url = createURL(path);
            } else {
                url = createURL(path, params);
            }

            if (payload == null) {
                return new UrlRequest(method, url, headers);
            }

            return new UrlRequest(method, url, headers) {
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
