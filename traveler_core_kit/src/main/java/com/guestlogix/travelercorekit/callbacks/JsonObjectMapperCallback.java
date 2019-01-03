package com.guestlogix.travelercorekit.callbacks;

public interface JsonObjectMapperCallback<T> {
    void onSuccess(T responseObject);

    void onError(Error error);
}