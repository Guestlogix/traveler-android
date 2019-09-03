package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public class BookableProduct implements Product {
    private String id;
    private Price price;
    private List<Pass> passes;
    private String title;
    private Date eventDate;

    BookableProduct(@NonNull String id, @NonNull Price price, @NonNull List<Pass> passes, String title, @NonNull Date eventDate) {
        this.id = id;
        this.price = price;
        this.passes = passes;
        this.title = title;
        this.eventDate = eventDate;
    }

    public List<Pass> getPasses() {
        return passes;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Price getPrice() {
        return price;
    }

    public Date getEventDate() {
        return eventDate;
    }
}
