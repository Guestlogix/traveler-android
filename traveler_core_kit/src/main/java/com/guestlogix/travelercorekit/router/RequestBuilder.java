package com.guestlogix.travelercorekit.router;

import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.tasks.NetworkTask.Request.Method;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.guestlogix.travelercorekit.utilities.UrlHelper.urlEncodeUTF8;

class RequestBuilder {
    private Method method;
    private String path;
    private String url;
    private JSONPayloadProvider payload = null;
    private String apiKey;
    private Map<String, String> headers;
    private List<String> params;

    private RequestBuilder() {
    }

    static RequestBuilder Builder() {
        return new RequestBuilder();
    }

    private static URL createURL(String url, String path, List<String> queryParams) {
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
