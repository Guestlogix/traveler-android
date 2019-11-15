package com.guestlogix.travelercorekit.models;

public class EphemeralKeyError extends Error {
    public enum Code {
        UNIDENTIFIED_TRAVELER
    }

    private Code code;

    EphemeralKeyError(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
