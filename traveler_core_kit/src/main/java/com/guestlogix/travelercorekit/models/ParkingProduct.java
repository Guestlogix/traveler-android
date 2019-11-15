package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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

    static class ParkingProductObjectMappingFactory implements ObjectMappingFactory<ParkingProduct> {
        @Override
        public ParkingProduct instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String title = jsonObject.getString("title");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("priceStartingAt").toString());
            ProductType productType = ProductType.fromString(jsonObject.getString("purchaseStrategy"));
            Coordinate coordinate = new Coordinate.CoordinateObjectMappingFactory()
                    .instantiate(jsonObject.getJSONObject("geoLocation").toString());
            ProviderTranslationAttribution providerTranslationAttribution = new ProviderTranslationAttribution.ProviderTranslationAttributionObjectMappingFactory().
                    instantiate(jsonObject.getJSONObject("providerTranslationAttribution").toString());

            Assertion.eval(id != null);
            Assertion.eval(price != null);

            return new ParkingProduct(id, title, price,
                    productType,
                    coordinate,
                    providerTranslationAttribution);
        }
    }
}
