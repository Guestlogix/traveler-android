package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

public class ParkingPurchasedProduct implements Product {
    private String id;
    private Price price;
    private ProductType productType = ProductType.PARKING;
    private String title;
    private Date eventDate;

    ParkingPurchasedProduct(
            @NonNull String id,
            String title,
            @NonNull Price price,
            @NonNull Date eventDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.eventDate = eventDate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Override
    public Price getPrice() {
        return price;
    }

    public Date getEventDate() {
        return eventDate;
    }

    @Override
    public ProductType getProductType() {
        return productType;
    }
}
