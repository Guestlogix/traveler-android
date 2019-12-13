package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PartnerOfferingProduct implements Product {
    private String id;
    private String title;
    private Price price;
    private List<ProductOffering> productOfferings;
    private ProductType productType = ProductType.PARTNER_OFFERING;
    private String cancellationPolicy;

    PartnerOfferingProduct(@NonNull String id,
                           String title,
                           @NonNull Price price,
                           @NonNull List<ProductOffering> productOfferings, String cancellationPolicy) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.productOfferings = productOfferings;
        this.cancellationPolicy = cancellationPolicy;
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

    public List<ProductOffering> getProductOfferings() {
        return productOfferings;
    }

    @Override
    public ProductType getProductType() {
        return productType;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }
}
