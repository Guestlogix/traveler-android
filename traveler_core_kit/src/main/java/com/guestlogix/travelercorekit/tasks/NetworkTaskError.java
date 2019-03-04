package com.guestlogix.travelercorekit.tasks;

public class NetworkTaskError extends Error {
    public enum Code {
        BAD_URL, CONNECTION_ERROR, NO_REQUEST, FORBIDDEN, UNAUTHORIZED, SERVER_ERROR
    }

    private Code code;
    private String message;

    NetworkTaskError(Code code) {
        super();

        this.code = code;
    }

    NetworkTaskError(Code code, String message) {
        super(message);

        this.code = code;
    }

    @Override
    public String toString() {
        switch (code) {
            case BAD_URL:
                return "Malformed URL";
            case FORBIDDEN:
                return "Access denied";
            case NO_REQUEST:
                return "No request";
            case SERVER_ERROR:
                return "Server Error";
            case UNAUTHORIZED:
                return "Not authorized";
            case CONNECTION_ERROR:
                return "Connection failed";
        }

        return "Unknown error";
    }

    public Code getCode() {
        return code;
    }
}
