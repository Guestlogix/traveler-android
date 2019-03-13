package com.guestlogix.travelercorekit;

import com.guestlogix.travelercorekit.tasks.NetworkTask;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Network Request
 */
public class UrlRequest implements NetworkTask.Request {
    protected Method method;
    protected URL url;
    protected String mToken;
    protected String apiKey;
    protected Map<String, String> headers;

    public UrlRequest(Method method, URL url) {
        this.url = url;
        this.method = method;
    }

    @Override
    public NetworkTask.Request.Method getMethod() {
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

    public JSONObject getJSONPayload() {
        return null;
    }
}
