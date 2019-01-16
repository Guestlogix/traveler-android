package com.guestlogix.travelercorekit.models;

import java.util.Date;

public class FlightQuery {
    String number;
    Date date;

    public FlightQuery(String number, Date date) {
        this.number = number;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }
}
