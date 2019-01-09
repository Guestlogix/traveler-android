package com.guestlogix.travelercorekit.error;

public class TravelerError {
    private TravelerErrorCode mCode;
    private String mMessage;

    public TravelerError(TravelerErrorCode code, String message) {
        mCode = code;
        mMessage = message;
    }

    public TravelerErrorCode getCode() {
        return mCode;
    }

    public String toString() {
        return mCode + mMessage;
    }
}
