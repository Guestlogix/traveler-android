package com.guestlogix.travelercorekit.utilities;

import com.guestlogix.travelercorekit.models.ObjectMappingError;

public class ObjectMappingException extends RuntimeException {
    private ObjectMappingError mError;

    public ObjectMappingException(ObjectMappingError error) {
        this.mError = error;
    }

    public ObjectMappingException(String error) {
        super(error);
    }

    public ObjectMappingError getError() {
        return mError;
    }

    @Override
    public String getMessage() {
        return mError.toString();
    }
}