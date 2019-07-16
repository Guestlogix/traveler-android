package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CatalogQuery implements Serializable {

    private List<Flight> flights;

    public CatalogQuery(List<Flight> flights) {
        this.flights = flights;
    }

    public CatalogQuery() { this(new ArrayList<>()); }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
