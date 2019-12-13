package com.guestlogix.travelercorekit.models;

import java.net.URL;

public class PartnerOfferingItem implements CatalogItem, Product {

    private String title, subtitle, id;
    private URL imageUrl;
    private boolean isAvailable;
    private Price price;

    PartnerOfferingItem(String id, String title,
                               String subtitle, URL imageUrl,
                               Price price, ProductType productType,
                               boolean isAvailable) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.id = id;
        this.isAvailable = isAvailable;
        this.price = price;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public URL getImageUrl() {
        return imageUrl;
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
