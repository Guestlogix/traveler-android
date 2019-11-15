package com.guestlogix.travelercorekit.models;

public class PartnerOfferingProduct implements Product {

    private String title, id;
    private boolean isAvailable;
    private Price price;

    public PartnerOfferingProduct(String id, String title,
                                  Price price, ProductType productType,
                                  boolean isAvailable) {
        this.title = title;
        this.id = id;
        this.isAvailable = isAvailable;
        this.price = price;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Price getPrice() {
        return price;
    }

    @Override
    public ProductType getProductType() {
        return ProductType.PARTNER_OFFERING;
    }

}
