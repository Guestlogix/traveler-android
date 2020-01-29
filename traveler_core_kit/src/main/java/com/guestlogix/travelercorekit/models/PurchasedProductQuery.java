package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class PurchasedProductQuery implements Serializable {
    private String orderId;
    private String productId;
    private ProductType productType;

    public PurchasedProductQuery(String orderId, String productId, ProductType productType) {
        this.orderId = orderId;
        this.productId = productId;
        this.productType = productType;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }

    public ProductType getProductType() {
        return productType;
    }
}
