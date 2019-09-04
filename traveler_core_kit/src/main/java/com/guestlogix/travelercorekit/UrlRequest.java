package com.guestlogix.travelercorekit;

import com.guestlogix.travelercorekit.tasks.NetworkTask;
import com.guestlogix.travelercorekit.tasks.NetworkTaskError;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Network Request
 */
public class UrlRequest implements NetworkTask.Route {
    protected Method method;
    protected URL url;
    Map<String, String> headers;

    public UrlRequest(Method method, URL url, Map<String, String> headers) {
        this.url = url;
        this.method = method;
        this.headers = headers;
    }

    public UrlRequest(Method method, URL url) {
        this.url = url;
        this.method = method;
        this.headers = null;
    }

    @Override
    public NetworkTask.Route.Method getMethod() {
        return this.method;
    }

    @Override
    public URL getURL() {
        return this.url;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void onProvidePayload(OutputStream stream) {
        JSONObject payload = getJSONPayload();

        if (payload == null) {
            return;
        }

        try {
            stream.write(payload.toString().getBytes());
        } catch (IOException e) {
            // TODO: Handle error
        }
    }

    @Override
    public Error transformError(NetworkTaskError error) {
        return error;
    }

    public JSONObject getJSONPayload() {
        return null;
    }
}
