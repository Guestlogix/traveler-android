package com.guestlogix.travelercorekit.network;

import com.guestlogix.travelercorekit.task.NetworkTask;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

class UnauthenticatedRequest implements NetworkTask.Request {
    private Method method;
    private URL url;
    private JSONObject payload;
    private String apiKey;
    private Map<String, String> headers;

    UnauthenticatedRequest(Method method, URL url, String apiKey, Map<String, String> headers) {
        this(method, url, apiKey, headers, null);
    }

    UnauthenticatedRequest(Method method, URL url, String apiKey, Map<String, String> headers, JSONObject payload) {
        this.method = method;
        this.url = url;
        this.apiKey = apiKey;
        this.payload = payload;
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() {

        headers.put("x-api-key", this.apiKey);

        return headers;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public void onProvidePayload(OutputStream stream) {
        if (payload == null) {
            return;
        }

        try {
            stream.write(payload.toString().getBytes());
        } catch (IOException e) {
            // TODO: Handle error
        }
    }
}
