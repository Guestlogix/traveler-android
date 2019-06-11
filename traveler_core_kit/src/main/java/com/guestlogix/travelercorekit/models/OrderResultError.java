package com.guestlogix.travelercorekit.models;

public class OrderResultError extends Error {
    public enum Code {
        UNIDENTIFIED_TRAVELER
    }

    private Code code;

    OrderResultError(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
