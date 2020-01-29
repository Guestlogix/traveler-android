package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ItineraryQuery implements Serializable {
    private List<Flight> flights;
    private Date startDate, endDate;

    public ItineraryQuery(List<Flight> flights, Date startDate, Date endDate) {
        this.flights = flights;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
