package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        public BookingItem instantiate(JsonReader reader) throws Exception {
            String id = null;
            String title = null;
            String subTitle = null;
            URL thumbnail = null;
            Price price = null;
            ProductType productType = null;
            List<BookingItemCategory> categories = null;
            Coordinate coordinate = null;
            ProviderTranslationAttribution providerTranslationAttribution = null;
            boolean isAvailable = false;
            boolean isWishlisted = false;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "subTitle":
                        subTitle = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "thumbnail":
                        JsonToken token = reader.peek();

                        if (token == JsonToken.NULL) {
                            reader.skipValue();
                            break;
                        }

                        thumbnail = new URL(reader.nextString());
                        break;
                    case "priceStartingAt":
                        price = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    case "purchaseStrategy":
                        productType = ProductType.fromString(reader.nextString());
                        break;
                    case "categories":
                        categories = new ArrayList<>();

                        reader.beginArray();

                        while (reader.hasNext()) {
                            categories.add(BookingItemCategory.fromString(reader.nextString()));
                        }

                        reader.endArray();
                        break;
                    case "geoLocation":
                        coordinate = new Coordinate.CoordinateObjectMappingFactory()
                                .instantiate(reader);
                        break;
                    case "providerTranslationAttribution":
                        providerTranslationAttribution = new ProviderTranslationAttribution.ProviderTranslationAttributionObjectMappingFactory().
                                instantiate(reader);
                        break;
                    case "isAvailable":
                        isAvailable = reader.nextBoolean();
                        break;
                    case "isWishlisted":
                        isWishlisted = reader.nextBoolean();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(id != null);
            Assertion.eval(price != null);
            Assertion.eval(categories != null);

            return new BookingItem(
                    title,
                    subTitle,
                    thumbnail,
                    new BookingProduct(id, title, price,
                            productType,
                            categories,
                            coordinate,
                            providerTranslationAttribution,
                            isAvailable,
                            isWishlisted));
        }
    }
}
