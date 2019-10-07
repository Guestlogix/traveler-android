package com.guestlogix.travelercorekit.models;

public class CancellationError extends Error {
    public enum Code {
        EXPIRED_QUOTE, NOT_CANCELLABLE, EXPLANATION_REQUIRED
    }

    private Code code;

    public CancellationError(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        switch (code) {
            case EXPIRED_QUOTE:
                return "Quote is expired";
            case NOT_CANCELLABLE:
                return "Order is not cancellable";
            case EXPLANATION_REQUIRED:
                return "An explanation is required";
                default:
                    return super.getMessage();
        }
    }
}
