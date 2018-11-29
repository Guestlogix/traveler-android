package com.guestlogix.network;

public class NetworkTask_<T> {

    private NetworkRequest mNetworkRequest;
    private MappingFactory<T> mFact;
    private JsonObjectMapperCallback mJsonObjectMapperCallback;

    public NetworkTask_(NetworkRequest mNetworkRequest, JsonObjectMapperCallback<T> mJsonObjectMapperCallback, MappingFactory<T> fact) {
        this.mNetworkRequest = mNetworkRequest;
        this.mJsonObjectMapperCallback = mJsonObjectMapperCallback;
        this.mFact = fact;
    }

    public JsonObjectMapperCallback getJsonObjectMapperCallback() {
        return mJsonObjectMapperCallback;
    }

    public MappingFactory<T> getmFact() {
        return mFact;
    }
}
