package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class Product implements Serializable {
    protected String id;
    protected Price price;

    Product(String id, Price price) throws IllegalArgumentException {
        if (null == id) {
            throw new IllegalArgumentException("id can not be null");
        } else {
            this.id = id;
        }
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
