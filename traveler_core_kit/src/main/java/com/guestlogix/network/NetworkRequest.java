package com.guestlogix.network;

import java.net.URL;
import java.util.Map;

public class NetworkRequest {

    private int mMethod;
    private URL mUrl;
    private Map<String, String> mPayload;
    private Map<String, String> mHeaders;

    /**
     * Supported request methods.
     */
    public interface Method {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int PATCH = 4;
    }

    /**
     * Creates a new network request with the given method, URL, payload and headers.
     */
    NetworkRequest(int method, URL url, Map<String, String> parameters, Map<String, String> headers) {
        mMethod = method;
        mUrl = url;
        mPayload = parameters;
        mHeaders = headers;
    }

    public int getMethod() {
        return mMethod;
    }

    public void semMethod(int mMethod) {
        this.mMethod = mMethod;
    }

    public URL getUrl() {
        return mUrl;
    }

    public void setUrl(URL mUrl) {
        this.mUrl = mUrl;
    }

    public Map<String, String> getPayload() {
        return mPayload;
    }

    public byte[] getPayloadAsBytesArray() {
        return mPayload.toString().getBytes();
    }

    public void setPayload(Map<String, String> mPayload) {
        this.mPayload = mPayload;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }
}
