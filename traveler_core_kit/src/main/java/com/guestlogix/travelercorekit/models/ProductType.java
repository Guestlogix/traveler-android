package com.guestlogix.travelercorekit.models;

public enum ProductType {
    BOOKABLE("Bookable"),
    PARKING("Parking"),
    PARTNER_OFFERING("Menu"),
    UNKNOWN("Unknown");

    private final String type;

    ProductType(String type) {
        this.type = type;
    }

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

    @Override
    public String toString() {
        return this.type;
    }
}
