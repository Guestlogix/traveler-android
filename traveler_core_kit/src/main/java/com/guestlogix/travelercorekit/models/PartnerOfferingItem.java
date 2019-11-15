package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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

    static class PartnerOfferingItemObjectMappingFactory implements ObjectMappingFactory<PartnerOfferingItem> {
        @Override
        public PartnerOfferingItem instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String title = jsonObject.getString( "title");
            String subTitle = jsonObject.getNullableString( "subTitle");
            URL thumbnail = null;
            if (!jsonObject.isNull("thumbnail"))
                thumbnail = new URL(jsonObject.getString("thumbnail"));

            PartnerOfferingProduct partnerOfferingProduct = new PartnerOfferingProduct.PartnerOfferingProductObjectMappingFactory().instantiate(rawResponse);

            return new PartnerOfferingItem(
                    title,
                    subTitle,
                    thumbnail,
                    partnerOfferingProduct);
        }
    }
}
