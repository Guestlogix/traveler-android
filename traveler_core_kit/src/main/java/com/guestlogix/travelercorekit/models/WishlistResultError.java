package com.guestlogix.travelercorekit.models;

public class WishlistResultError extends Error {
    private static final String UNIDENTIFIED_TRAVELER_ERROR = "You have to login first to wishlist items. Identify by calling Traveler.identify()";
    public enum Code {
        UNIDENTIFIED_TRAVELER
    }

    private Code code;

    WishlistResultError(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        if (code == Code.UNIDENTIFIED_TRAVELER) {
            return UNIDENTIFIED_TRAVELER_ERROR;
        }
        return super.getMessage();
    }
}
