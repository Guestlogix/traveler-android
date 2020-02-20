package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
            boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.productType = productType;
        this.categories = categories;
        this.coordinate = coordinate;
        this.providerTranslationAttribution = providerTranslationAttribution;
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

    public void setIsWishlisted(Boolean wishlisted) {
        isWishlisted = wishlisted;
    }

    static class BookingProductObjectMappingFactory implements ObjectMappingFactory<BookingProduct> {
        @Override
        public BookingProduct instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String title = jsonObject.getString("title");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("priceStartingAt").toString());
            ProductType productType = ProductType.fromString(jsonObject.getString("purchaseStrategy"));

            List<BookingItemCategory> categories = new ArrayMappingFactory<>(new BookingItemCategory.CategoryObjectMappingFactory()).instantiate(jsonObject.getJSONArray("subCategories").toString());

            Coordinate coordinate = new Coordinate.CoordinateObjectMappingFactory()
                    .instantiate(jsonObject.getJSONObject("geoLocation").toString());

            ProviderTranslationAttribution providerTranslationAttribution = new ProviderTranslationAttribution.ProviderTranslationAttributionObjectMappingFactory().
                    instantiate(jsonObject.getJSONObject("providerTranslationAttribution").toString());
            boolean isAvailable = jsonObject.getBoolean("isAvailable");

            Assertion.eval(id != null);
            Assertion.eval(price != null);
            Assertion.eval(categories != null);

            return new BookingProduct(id, title, price,
                    productType,
                    categories,
                    coordinate,
                    providerTranslationAttribution,
                    isAvailable);
        }
    }
}
