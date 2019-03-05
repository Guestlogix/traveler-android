package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class Product implements Serializable {
    protected String id;
    protected Price price;

    protected Product(String id, Price price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    protected void setPrice(Price price) {
        this.price = price;
    }
}
