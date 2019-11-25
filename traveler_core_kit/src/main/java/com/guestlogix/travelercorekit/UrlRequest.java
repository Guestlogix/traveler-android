package com.guestlogix.travelercorekit;

import android.util.Log;

import com.guestlogix.travelercorekit.tasks.NetworkTask;
import com.guestlogix.travelercorekit.tasks.NetworkTaskError;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
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
        if (BuildConfig.DEBUG)
            logNetworkRequest(getPayload());

        if (getPayload() == null) {
            return;
        }

        try {
            stream.write(getPayload().getBytes());
        } catch (IOException e) {
            // TODO: Handle error
        }
    }

    @Override
    public Error transformError(NetworkTaskError error) {
        return error;
    }

    public String getPayload() {
        return null;
    }

    private void logNetworkRequest(String postBody) {
        //do not print request if its a file
        if (url.toString().endsWith("jpg")||
                url.toString().endsWith("png")||
                url.toString().endsWith("gif")||
                url.toString().endsWith("bmp"))
            return;

        StringBuilder headersForLog = new StringBuilder();

        if (headers != null) {
            for (String key : headers.keySet()) {
                headersForLog.append(key + " : " + headers.get(key) + "\n");
            }
        }

        Log.d("NetworkTask",
                        " \n POST BODY:" + ((postBody == null || postBody.trim().isEmpty()) ? " [NO POST BODY]" : "\n" + postBody) +
                        "\n------------------------\n \n");
    }

}
