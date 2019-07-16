package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class FlightQuery implements Serializable {
    private String number;
    private Date date;

    public FlightQuery(@NonNull String number, @NonNull Date date) {
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
