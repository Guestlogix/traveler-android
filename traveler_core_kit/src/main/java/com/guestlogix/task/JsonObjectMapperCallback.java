package com.guestlogix.task;

public interface JsonObjectMapperCallback<T> {
    void onSuccess(T responseObject);

    void onError(Error error);
}