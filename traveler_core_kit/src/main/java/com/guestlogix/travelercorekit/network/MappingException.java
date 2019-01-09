package com.guestlogix.travelercorekit.network;

import com.guestlogix.travelercorekit.error.TravelerError;

public class MappingException extends Exception {
    private TravelerError mError;

    public MappingException(TravelerError error) {
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