package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

public class ParkingProduct implements Product {
    private String id;
    private String title;
    private Price price;
    private ProductType productType;
    private Coordinate coordinate;
    private ProviderTranslationAttribution providerTranslationAttribution;

    ParkingProduct(
            @NonNull String id,
            String title,
            Price price,
            ProductType productType,
            Coordinate coordinate,
            ProviderTranslationAttribution providerTranslationAttribution) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.productType = productType;
        this.coordinate = coordinate;
        this.providerTranslationAttribution = providerTranslationAttribution;
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

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public ProviderTranslationAttribution getProviderTranslationAttribution() {
        return providerTranslationAttribution;
    }

    public boolean isAvailable() {
        // A ParkingItem that is seen is always available
        return true;
    }
}
