package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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

    static class PartnerOfferingProductObjectMappingFactory implements ObjectMappingFactory<PartnerOfferingProduct> {
        @Override
        public PartnerOfferingProduct instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString( "id");
            String title = jsonObject.getString( "title");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("priceStartingAt").toString());
            ProductType productType = ProductType.fromString(jsonObject.getString("purchaseStrategy"));
            boolean isAvailable = jsonObject.getBoolean("isAvailable");

            Assertion.eval(price != null);

            return new PartnerOfferingProduct(id, title, price,
                    productType,
                    isAvailable);
        }
    }
}
