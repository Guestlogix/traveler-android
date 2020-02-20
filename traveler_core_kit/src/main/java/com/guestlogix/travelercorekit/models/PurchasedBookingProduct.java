package com.guestlogix.travelercorekit.models;

import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
    private BookingItemDetails bookingItemDetails;


    public PurchasedBookingProduct(String id,
                                   String title,
                                   Price price,
                                   List<Pass> passes,
                                   Date eventDate,
                                   List<BookingItemCategory> categories,
                                   String cancellationPolicy) {
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
        return ProductType.BOOKABLE;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public BookingItemDetails getBookingItemDetails() {
        return bookingItemDetails;
    }

    private void setBookingItemDetails(BookingItemDetails bookingItemDetails) {
        this.bookingItemDetails = bookingItemDetails;
    }

    static class BookingPurchasedProductObjectMappingFactory implements ObjectMappingFactory<PurchasedBookingProduct> {
        @Override
        public PurchasedBookingProduct instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            //TODO: purchased product in itinerary are different form the one in order :/ fix this when backend devs could digest OOP
            if (!jsonObject.isNull("orderProduct")) {
                PurchasedBookingProduct purchasedBookingProduct = instantiate(jsonObject.getJSONObject("orderProduct").toString());
                BookingItemDetails bookingItemDetails = null;
                if (!jsonObject.isNull("itemDetail")) {
                    bookingItemDetails = new BookingItemDetails.BookingItemDetailsObjectMappingFactory().instantiate(jsonObject.getJSONObject("itemDetail").toString());
                }
                purchasedBookingProduct.setBookingItemDetails(bookingItemDetails);
                return purchasedBookingProduct;
            }
            else
            {
                String id = jsonObject.getString("id");
                String title = jsonObject.getNullableString("title");
                Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("price").toString());
                List<Pass> passes = new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()).instantiate(jsonObject.getJSONArray("passes").toString());
                Date eventDate = DateHelper.parseISO8601(jsonObject.getString("experienceDate"));
                List<BookingItemCategory> categories = new ArrayMappingFactory<>(new BookingItemCategory.CategoryObjectMappingFactory()).instantiate(jsonObject.getJSONArray("subCategories").toString());
                String cancellationPolicy = jsonObject.getString("cancellationPolicy");
                return new PurchasedBookingProduct(id, title, price, passes, eventDate, categories, cancellationPolicy);
            }

        }
    }
}
