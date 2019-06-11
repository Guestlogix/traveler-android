package com.guestlogix.travelercorekit.utilities;

public class ObjectMappingException extends RuntimeException {
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