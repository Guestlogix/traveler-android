package com.guestlogix.travelercorekit.models;

import java.util.ArrayList;

public class CatalogQuery {

    private ArrayList<Flight> flights;

    public CatalogQuery(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }
}
