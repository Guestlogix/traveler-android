package com.guestlogix.travelercorekit.utilities;

public interface JsonObjectMapperCallback<T> {
    void onSuccess(T responseObject);
    void onError(Error error);
}