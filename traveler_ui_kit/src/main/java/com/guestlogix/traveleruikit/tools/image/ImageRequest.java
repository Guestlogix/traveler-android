package com.guestlogix.traveleruikit.tools.image;

import com.guestlogix.travelercorekit.tasks.NetworkTask;

import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Network Request for Images
 */
public class ImageRequest implements NetworkTask.Request {
    private URL url;

    public ImageRequest(URL url) {
        this.url = url;
    }

    @Override
    public NetworkTask.Request.Method getMethod() {
        return Method.GET;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<>();
    }

    @Override
    public void onProvidePayload(OutputStream stream) {

    }
}
