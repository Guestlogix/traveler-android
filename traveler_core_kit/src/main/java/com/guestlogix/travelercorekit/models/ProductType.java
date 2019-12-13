package com.guestlogix.travelercorekit.models;

public enum ProductType {
    BOOKABLE, PARKING, PARTNER_OFFERING;

    public static ProductType fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Bookable":
                return BOOKABLE;
            case "Parking":
                return PARKING;
            case "Menu":
                return PARTNER_OFFERING;
            default:
                throw new IllegalArgumentException("Unknown ProductType: " + value);
        }
    }
}
