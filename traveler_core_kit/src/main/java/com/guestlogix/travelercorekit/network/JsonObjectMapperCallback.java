package com.guestlogix.travelercorekit.network;

public interface JsonObjectMapperCallback<T> {
    void onSuccess(T responseObject);

    void onError(Error error);
}