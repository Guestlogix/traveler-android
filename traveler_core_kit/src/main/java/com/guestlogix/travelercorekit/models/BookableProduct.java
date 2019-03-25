package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookableProduct implements Product {
    private String id;
    private Price price;
    private List<Pass> passes;
    private String title;

    @SuppressWarnings("ConstantConditions")
    BookableProduct(@NonNull String id, @NonNull Price price, @NonNull List<Pass> passes, String title) throws IllegalArgumentException {
        if (passes == null) {
            throw new IllegalArgumentException("passes can not be null");
        }

        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }

        if (price == null) {
            throw new IllegalArgumentException("price can not be null");
        }

        this.id = id;
        this.price = price;
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Price getPrice() {
        return price;
    }
}
