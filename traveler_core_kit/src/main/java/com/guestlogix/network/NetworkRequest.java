package com.guestlogix.network;

import java.util.Map;

public class NetworkRequest {

    private int mMethod;
    private String mUrl;
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
    }

    /**
     * Creates a new network request with the given method, URL, payload and headers.
     */
    NetworkRequest(int method, String url, Map<String, String> parameters, Map<String, String> headers) {
        mMethod = method;
        mUrl = url;
        mPayload = parameters;
        mHeaders = headers;
    }

    public int getmMethod() {
        return mMethod;
    }

    public void setmMethod(int mMethod) {
        this.mMethod = mMethod;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Map<String, String> getmPayload() {
        return mPayload;
    }

    public void setmPayload(Map<String, String> mPayload) {
        this.mPayload = mPayload;
    }

    public Map<String, String> getmHeaders() {
        return mHeaders;
    }

    public void setmHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }
}
