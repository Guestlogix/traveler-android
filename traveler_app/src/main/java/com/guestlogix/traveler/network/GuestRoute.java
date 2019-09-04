package com.guestlogix.traveler.network;

import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.UrlRequest;
import com.guestlogix.travelercorekit.tasks.NetworkTask;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.guestlogix.travelercorekit.utilities.UrlHelper.urlEncodeUTF8;

// TODO: Redo this class
// Model after Router in Core

class GuestRoute {

    private static final String BASE_URL = "https://hklkg7c974.execute-api.ca-central-1.amazonaws.com/dev";

    static UrlRequest profile(String requestIdToken) {
        return GuestRequestBuilder.Builder()
                .method(NetworkTask.Route.Method.GET)
                .url(BASE_URL)
                .path("/login")
                .headers(buildHeaders(requestIdToken))
                .build();
    }

    /**
     * Generates headers with required fields.
     *
     * @return Map of headers containing device info.
     */
    private static Map<String, String> buildHeaders(@NonNull String requestIdToken) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("x-access-token", requestIdToken);
        return headers;
    }

    private static class GuestRequestBuilder {
        private NetworkTask.Route.Method method;
        private String path;
        private String url;
        private GuestRequestBuilder.JSONPayloadProvider payload = null;
        private Map<String, String> headers;
        private List<String> params;

        private GuestRequestBuilder() {
        }

        public static GuestRequestBuilder Builder() {
            return new GuestRequestBuilder();
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

        public GuestRequestBuilder method(NetworkTask.Route.Method method) {
            this.method = method;
            return this;
        }

        public GuestRequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        public GuestRequestBuilder url(String url) {
            this.url = url;
            return this;
        }

        public GuestRequestBuilder param(String key, String value) {
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

        public GuestRequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public UrlRequest build() {
            URL url;
            if (params == null) {
                url = createURL(this.url, path);
            } else {
                url = createURL(this.url, path, params);
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
