package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookingItem implements CatalogItem, Product {
    private String id;
    private String title;
    private String subTitle;
    private URL imageURL;
    private Price price;
    private ProductType productType;
    private List<ProductItemCategory> categories;
    private Coordinate coordinate;
    private ProviderTranslationAttribution providerTranslationAttribution;

    BookingItem(
            @NonNull String id,
            String title,
            String subTitle,
            URL imageURL,
            Price price,
            ProductType productType,
            List<ProductItemCategory> categories,
            Coordinate coordinate,
            ProviderTranslationAttribution providerTranslationAttribution) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
        this.price = price;
        this.productType = productType;
        this.categories = categories;
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
    public String getSubTitle() {
        return subTitle;
    }

    @Override
    public URL getImageURL() {
        return imageURL;
    }

    @Override
    public Price getPrice() {
        return price;
    }

    @Override
    public ProductType getProductType() {
        return productType;
    }

    @Override
    public List<ProductItemCategory> getCategories() {
        return categories;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public ProviderTranslationAttribution getProviderTranslationAttribution() {
        return providerTranslationAttribution;
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
            List<ProductItemCategory> categories = null;
            Coordinate coordinate = null;
            ProviderTranslationAttribution providerTranslationAttribution = null;

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
                            categories.add(ProductItemCategory.fromString(reader.nextString()));
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
                    id,
                    title,
                    subTitle,
                    thumbnail,
                    price,
                    productType,
                    categories,
                    coordinate,
                    providerTranslationAttribution);
        }
    }
}
