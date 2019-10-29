package com.guestlogix.travelercorekit.models;

public class PaymentError extends Error {
    public enum Code {
        PROCESSING_ERROR,
        CONFIRMATION_REQUIRED,
        CONFIRMATION_FAILED
    }

    private Code code;
    private String data;

    public PaymentError(Code code, String data) {
        this.code = code;
        this.data = data;
    }

    public Code getCode() {
        return code;
    }

    public String getData() { return data; }
}
