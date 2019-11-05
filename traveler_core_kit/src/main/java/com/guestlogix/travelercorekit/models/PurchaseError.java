package com.guestlogix.travelercorekit.models;

public class PurchaseError extends Error {
    public enum Code {
        NO_DATE, BAD_DATE, NO_OPTION, NO_PASSES, VERY_OLD_TRAVELER, ADULT_AGE_INVALID, BELLOW_MIN_UNITS, UNACCOMPANIED_CHILDREN, PASSES_UNAVAILABLE
    }

    private Code code;

    public PurchaseError(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        switch (code) {
            case NO_DATE:
                return "No date provided";
            case BAD_DATE:
                return "Date not available";
            case NO_OPTION:
                return "No option provided";
            case NO_PASSES:
                return "No passes selected";
            case BELLOW_MIN_UNITS:
                return "Passes bellow minimum";
            case ADULT_AGE_INVALID:
                return "Invalid age for adult";
            case VERY_OLD_TRAVELER:
                return "Traveler too old";
            case UNACCOMPANIED_CHILDREN:
                return "Unaccompanied children";
            case PASSES_UNAVAILABLE:
                return "Number of passes unavailable";
                default:
                    return super.getMessage();
        }
    }
}
