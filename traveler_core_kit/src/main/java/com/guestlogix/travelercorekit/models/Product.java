package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class Product implements Serializable {
    String id;
    Price price;

    public Product(String id, Price price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
