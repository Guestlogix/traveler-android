package com.guestlogix.travelercorekit.network;

import com.guestlogix.travelercorekit.error.TravelerError;

public class ObjectMappingException extends Exception {
    private TravelerError mError;

    public ObjectMappingException(TravelerError error) {
        this.mError = error;
    }

    public TravelerError getError() {
        return mError;
    }

    @Override
    public String getMessage() {
        return mError.toString();
    }
}