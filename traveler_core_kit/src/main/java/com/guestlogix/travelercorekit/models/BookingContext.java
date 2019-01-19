package com.guestlogix.travelercorekit.models;

public class BookingContext {

    Product product;

    public BookingContext(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
