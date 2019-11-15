package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import java.util.List;

public class BookingProduct implements Product {
    private String id;
    private String title;
    private Price price;

    private ProductType productType;
    private List<BookingItemCategory> categories;
    private Coordinate coordinate;
    private ProviderTranslationAttribution providerTranslationAttribution;
    private Boolean isAvailable;
    private Boolean isWishlisted;

    BookingProduct(
            @NonNull String id,
            String title,
            Price price,
            ProductType productType,
            List<BookingItemCategory> categories,
            Coordinate coordinate,
            ProviderTranslationAttribution providerTranslationAttribution,
            boolean isAvailable,
            boolean isWishlisted) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.productType = productType;
        this.categories = categories;
        this.coordinate = coordinate;
        this.providerTranslationAttribution = providerTranslationAttribution;
        this.isWishlisted = isWishlisted;
        this.isAvailable = isAvailable;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }


    @Override
    public Price getPrice() {
        return price;
    }

    @Override
    public ProductType getProductType() {
        return productType;
    }

    public List<BookingItemCategory> getCategories() {
        return categories;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public ProviderTranslationAttribution getProviderTranslationAttribution() {
        return providerTranslationAttribution;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Boolean isWishlisted() {
        return isWishlisted;
    }

    void setWishlisted(Boolean wishlisted) {
        isWishlisted = wishlisted;
    }
}
