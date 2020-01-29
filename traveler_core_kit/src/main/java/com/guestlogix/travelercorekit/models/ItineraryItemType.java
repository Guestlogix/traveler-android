package com.guestlogix.travelercorekit.models;

public enum ItineraryItemType {
    BOOKING, PARKING, TRANSPORTATION, FLIGHT, UNKNOWN;

    public static ItineraryItemType fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "OrderBooking":
                return BOOKING;
            case "OrderParking":
                return PARKING;
            case "OrderTransportation":
                return TRANSPORTATION;
            case "Flight":
                return FLIGHT;
            default:
                return UNKNOWN;
        }
    }
}
