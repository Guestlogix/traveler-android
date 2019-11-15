package com.guestlogix.travelercorekit.models;

public enum QueryType {
    BOOKING, PARKING, UNKNOWN;

    public static QueryType fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Booking":
                return BOOKING;
            case "Parking":
                return PARKING;
            default:
                return UNKNOWN;
        }
    }
}
