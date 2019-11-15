package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.BookingCategoryArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.util.Date;
import java.util.List;

public class PurchasedBookingProduct implements Product {
    public static final String CANCELLATION_POLICY_NON_REFUNDABLE = "Non-refundable";
    private String id;
    private String title;
    private Price price;
    private List<Pass> passes;
    private Date eventDate;
    private List<BookingItemCategory> categories;
    private ProductType productType = ProductType.BOOKABLE;
    private String cancellationPolicy;

    PurchasedBookingProduct(@NonNull String id,
                            String title,
                            @NonNull Price price,
                            @NonNull List<Pass> passes,
                            @NonNull Date eventDate,
                            List<BookingItemCategory> categories,
                            @NonNull String cancellationPolicy) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.passes = passes;
        this.eventDate = eventDate;
        this.categories = categories;
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

    public List<Pass> getPasses() {
        return passes;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public List<BookingItemCategory> getCategories() {
        return categories;
    }

    @Override
    public ProductType getProductType() {
        return productType;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    static class BookingPurchasedProductObjectMappingFactory implements ObjectMappingFactory<PurchasedBookingProduct> {
        @Override
        public PurchasedBookingProduct instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String id = jsonObject.getString("id");
            String title = jsonObject.getNullableString( "title");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("price").toString());
            List<Pass> passes = new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()).instantiate(jsonObject.getJSONArray("passes").toString());
            Date eventDate = DateHelper.parseISO8601(jsonObject.getString("experienceDate"));
            List<BookingItemCategory> categories = new BookingCategoryArrayMappingFactory().instantiate(jsonObject.getJSONArray("categories").toString());


            String cancellationPolicy = jsonObject.getString("cancellationPolicy");

            Assertion.eval(id != null);
            Assertion.eval(title != null);
            Assertion.eval(price != null);
            Assertion.eval(eventDate != null);

            return new PurchasedBookingProduct(id, title, price, passes, eventDate, categories, cancellationPolicy);
        }
    }
}
