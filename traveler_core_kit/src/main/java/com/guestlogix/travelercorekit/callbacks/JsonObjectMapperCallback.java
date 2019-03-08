package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.ObjectMappingError;

public interface JsonObjectMapperCallback<T> {
    void onSuccess(T responseObject);

    void onError(ObjectMappingError error);
}