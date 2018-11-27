package com.guestlogix.network;

public class NetworkTask {

    private NetworkRequest mNetworkRequest;
    private JsonObjectMapperCallback mJsonObjectMapperCallback;

    public NetworkTask(NetworkRequest mNetworkRequest, JsonObjectMapperCallback mJsonObjectMapperCallback) {
        this.mNetworkRequest = mNetworkRequest;
        this.mJsonObjectMapperCallback = mJsonObjectMapperCallback;
    }

    public NetworkRequest getmNetworkRequest() {
        return mNetworkRequest;
    }

    public void setmNetworkRequest(NetworkRequest mNetworkRequest) {
        this.mNetworkRequest = mNetworkRequest;
    }

    public JsonObjectMapperCallback getmJsonObjectMapperCallback() {
        return mJsonObjectMapperCallback;
    }

    public void setmJsonObjectMapperCallback(JsonObjectMapperCallback mJsonObjectMapperCallback) {
        this.mJsonObjectMapperCallback = mJsonObjectMapperCallback;
    }
}
