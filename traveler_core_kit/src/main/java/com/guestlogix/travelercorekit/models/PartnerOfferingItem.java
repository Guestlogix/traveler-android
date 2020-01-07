package com.guestlogix.travelercorekit.models;

import java.net.URL;

public class PartnerOfferingItem implements CatalogItem<PartnerOfferingProduct> {

    private String title, subtitle;
    private URL imageUrl;
    private PartnerOfferingProduct PartnerOfferingProduct;

    public PartnerOfferingItem(String title,
                               String subtitle, URL imageUrl,
                               PartnerOfferingProduct PartnerOfferingProduct) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.PartnerOfferingProduct = PartnerOfferingProduct;
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

    @Override
    public PartnerOfferingProduct getItemResource() {
        return PartnerOfferingProduct;
    }
}
