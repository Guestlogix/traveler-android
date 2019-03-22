package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class Product implements Serializable {
    protected String id;
    protected Price price;
    protected PurchaseStrategy purchaseStrategy;

    Product(String id, Price price, PurchaseStrategy purchaseStrategy) throws IllegalArgumentException {
        if (null == id) {
            throw new IllegalArgumentException("id can not be null");
        } else {
            this.id = id;
        }
        this.price = price;
        this.purchaseStrategy = purchaseStrategy;
    }

    Product(String id, Price price) throws IllegalArgumentException {
        this(id, price, null);
    }

    public String getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    public PurchaseStrategy getPurchaseStrategy() {
        return purchaseStrategy;
    }

    protected void setPrice(Price price) {
        this.price = price;
    }
}
