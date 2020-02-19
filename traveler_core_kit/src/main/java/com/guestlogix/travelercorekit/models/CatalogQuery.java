package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CatalogQuery implements Serializable {

    private List<Flight> flights;
    private List<Product> products;
    private String city;

    public CatalogQuery(List<Flight> flights) {
        this(flights, new ArrayList<>());
    }

    public CatalogQuery(List<Flight> flights, List<Product> products) {
        this.flights = flights;
        this.products = products;
    }

    public CatalogQuery(String city) {
        this.city = city;
    }

    public CatalogQuery() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
