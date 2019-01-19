package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class Product implements Serializable {
    String id;
    double price;

    public Product(String id, double price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
