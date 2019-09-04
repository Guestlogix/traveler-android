package com.guestlogix.travelercorekit.tasks;

public class NetworkTaskError extends Error {
    public enum Code {
        BAD_URL, CONNECTION_ERROR, NO_ROUTE, FORBIDDEN, UNAUTHORIZED, SERVER_ERROR
    }

    private Code code;

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
        return String.format("%s %s", getCodeValue(), super.toString());
    }

    @Override
    public String getMessage() {
        return String.format("%s %s", getCodeValue(), super.getMessage());
    }

    public Code getCode() {
        return code;
    }

    private String getCodeValue() {

        switch (code) {
            case BAD_URL:
                return "Malformed URL";
            case FORBIDDEN:
                return "Access Denied";
            case NO_ROUTE:
                return "No Route";
            case SERVER_ERROR:
                return "Server Error";
            case UNAUTHORIZED:
                return "Not Authorized";
            case CONNECTION_ERROR:
                return "Connection Failed";
        }
        return "Unknown Error";
    }
}
