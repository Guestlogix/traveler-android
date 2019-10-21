package com.guestlogix.travelercorekit.models;

public enum ProductType {
    BOOKABLE, PARKING;

    public static ProductType fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Bookable":
                return BOOKABLE;
            case "Parking":
                return PARKING;
            default:
                throw new IllegalArgumentException("Unknown ProductType");
        }
    }
}
