package com.guestlogix.travelercorekit.utilities;

import com.guestlogix.travelercorekit.models.TravelerError;

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