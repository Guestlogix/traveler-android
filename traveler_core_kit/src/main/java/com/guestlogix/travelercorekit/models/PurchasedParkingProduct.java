package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.util.Date;

public class PurchasedParkingProduct implements Product {
    private String id;
    private Price price;
    private ProductType productType = ProductType.PARKING;
    private String title;
    private Date eventDate;

    PurchasedParkingProduct(
            @NonNull String id,
            String title,
            @NonNull Price price,
            @NonNull Date eventDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.eventDate = eventDate;
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

    public Date getEventDate() {
        return eventDate;
    }

    @Override
    public ProductType getProductType() {
        return productType;
    }

    static class ParkingPurchasedProductObjectMappingFactory implements ObjectMappingFactory<PurchasedParkingProduct> {
        @Override
        public PurchasedParkingProduct instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String id = jsonObject.getString("id");
            String title = jsonObject.getNullableString( "title");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("price").toString());
            Date eventDate = DateHelper.parseISO8601(jsonObject.getString("experienceDate"));

            Assertion.eval(id != null);
            Assertion.eval(title != null);
            Assertion.eval(price != null);
            Assertion.eval(eventDate != null);

            return new PurchasedParkingProduct(id, title, price, eventDate);
        }
    }
}
