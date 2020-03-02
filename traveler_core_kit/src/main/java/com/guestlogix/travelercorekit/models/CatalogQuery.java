package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CatalogQuery implements Serializable {

    private List<Flight> flights;
    private List<Product> products;
    private Coordinate location;
    private String city;

    public CatalogQuery(List<Flight> flights) {
        this(flights, new ArrayList<>(), null);
    }

    public CatalogQuery(List<Flight> flights, Coordinate location) {
        this(flights, new ArrayList<>(), null);
    }

    public CatalogQuery(List<Flight> flights, List<Product> products) {
        this.flights = flights;
        this.products = products;
    }

    public CatalogQuery(List<Flight> flights, List<Product> products, Coordinate location) {
        this.flights = flights;
        this.products = products;
        this.location = location;
    }

    public CatalogQuery(String city) {
        this.city = city;
    }

    public CatalogQuery(String city, Coordinate location) {
        this.city = city;
        this.location = location;
    }

    public CatalogQuery() {
        this(new ArrayList<>(), new ArrayList<>(), null);
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

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }
}
