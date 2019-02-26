package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.TravelerError;

public interface JsonObjectMapperCallback<T> {
    void onSuccess(T responseObject);

    void onError(TravelerError error);
}