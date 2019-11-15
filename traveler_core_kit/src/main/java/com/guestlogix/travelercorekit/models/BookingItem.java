package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;

public class BookingItem implements CatalogItem<BookingProduct> {
    private String title;
    private String subTitle;
    private URL imageURL;
    private BookingProduct bookingProduct;

    BookingItem(
            String title,
            String subTitle,
            URL imageURL,
            BookingProduct bookingProduct) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
        this.bookingProduct = bookingProduct;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSubtitle() {
        return subTitle;
    }

    @Override
    public URL getImageUrl() {
        return imageURL;
    }

    @Override
    public BookingProduct getItemResource() {
        return bookingProduct;
    }

    static class BookingItemObjectMappingFactory implements ObjectMappingFactory<BookingItem> {
        @Override
        public BookingItem instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String title = jsonObject.getNullableString( "title");
            String subTitle = jsonObject.getNullableString( "subTitle");
            URL thumbnail = null;
            if (!jsonObject.isNull("thumbnail"))
                thumbnail = new URL(jsonObject.getString("thumbnail"));
            BookingProduct bookingProduct = new BookingProduct.BookingProductObjectMappingFactory().instantiate(rawResponse);

            return new BookingItem(
                    title,
                    subTitle,
                    thumbnail,
                    bookingProduct);
        }
    }
}
