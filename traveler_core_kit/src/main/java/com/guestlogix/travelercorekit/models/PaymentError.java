package com.guestlogix.travelercorekit.models;

public class PaymentError extends Error {
    public enum Code {
        PROCESSING_ERROR
    }

    private Code code;

    public PaymentError(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
