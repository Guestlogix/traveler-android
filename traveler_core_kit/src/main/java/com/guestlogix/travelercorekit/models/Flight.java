package com.guestlogix.travelercorekit.models;

import java.util.Date;

public class Flight {
    String id;
    String number;
    Airport departureAirport;
    Airport arrivalAirport;
    Date departureDate;
    Date arrivalDate;

    public Flight(String id, String number, Airport departureAirport, Airport arrivalAirport, Date departureDate, Date arrivalDate) {
        this.id = id;
        this.number = number;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }
}
