package com.guestlogix.travelercorekit.models;

public enum ProductType {
    BOOKABLE, PARKING, PARTNER_OFFERING, UNKNOWN;

    public static ProductType fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Bookable":
                return BOOKABLE;
            case "Parking":
                return PARKING;
            case "Menu":
                return PARTNER_OFFERING;
            default:
                return UNKNOWN;
        }
    }
}
