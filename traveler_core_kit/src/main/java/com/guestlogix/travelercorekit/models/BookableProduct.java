package com.guestlogix.travelercorekit.models;

import androidx.annotation.Nullable;

import java.util.List;

public class BookableProduct extends Product {
    private List<Pass> passes;
    private String title;

    BookableProduct(String id, Price price, List<Pass> passes, String title) throws IllegalArgumentException {
        super(id, price);

        if (passes == null) {
            throw new IllegalArgumentException("passes can not be null");
        }

        this.passes = passes;
        this.title = title;
    }

    public List<Pass> getPasses() {
        return passes;
    }

    @Nullable
    public String getTitle() {
        return title;
    }
}
