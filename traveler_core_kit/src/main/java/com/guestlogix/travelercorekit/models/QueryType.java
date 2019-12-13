package com.guestlogix.travelercorekit.models;

public enum QueryType {
    BOOKING, PARKING;

    public static QueryType fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Booking":
                return BOOKING;
            case "Parking":
                return PARKING;
            default:
                throw new IllegalArgumentException("Unknown QueryType: " + value);
        }
    }
}
