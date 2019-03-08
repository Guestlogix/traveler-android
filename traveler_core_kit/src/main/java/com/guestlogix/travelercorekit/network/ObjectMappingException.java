package com.guestlogix.travelercorekit.network;

import com.guestlogix.travelercorekit.models.ObjectMappingError;

public class ObjectMappingException extends Exception {
    private ObjectMappingError mError;

    public ObjectMappingException(ObjectMappingError error) {
        this.mError = error;
    }

    public ObjectMappingError getError() {
        return mError;
    }

    @Override
    public String getMessage() {
        return mError.toString();
    }
}