package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public class BookingProduct implements Product {
    private String id;
    private String title;
    private Price price;
    private List<Pass> passes;
    private Date eventDate;
    private List<ProductItemCategory> categories;
    private ProductType productType = ProductType.BOOKABLE;

    BookingProduct(
            @NonNull String id,
            String title,
            @NonNull Price price,
            @NonNull List<Pass> passes,
            @NonNull Date eventDate,
            List<ProductItemCategory> categories) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.passes = passes;
        this.eventDate = eventDate;
        this.categories = categories;
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

    public List<Pass> getPasses() {
        return passes;
    }

    public Date getEventDate() {
        return eventDate;
    }

    @Override
    public List<ProductItemCategory> getCategories() {
        return categories;
    }

    @Override
    public ProductType getProductType() {
        return productType;
    }
}
