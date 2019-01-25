package com.guestlogix.travelercorekit.models;

import java.util.List;

public class CatalogQuery {

    private List<Flight> flights;

    public CatalogQuery(List<Flight> flights) {
        this.flights = flights;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
