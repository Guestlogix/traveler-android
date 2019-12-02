package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;

public class ParkingItem implements CatalogItem, Product {
    private String id;
    private String title;
    private String subTitle;
    private URL imageURL;
    private Price price;
    private ProductType productType;
    private Coordinate coordinate;
    private ProviderTranslationAttribution providerTranslationAttribution;

    ParkingItem(
            @NonNull String id,
            String title,
            String subTitle,
            URL imageURL,
            Price price,
            ProductType productType,
            Coordinate coordinate,
            ProviderTranslationAttribution providerTranslationAttribution) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
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

    static class ParkingItemObjectMappingFactory implements ObjectMappingFactory<ParkingItem> {
        @Override
        public ParkingItem instantiate(JsonReader reader) throws Exception {
            String id = null;
            String title = null;
            String subTitle = null;
            URL thumbnail = null;
            Price price = null;
            ProductType productType = null;
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

            return new ParkingItem(
                    id,
                    title,
                    subTitle,
                    thumbnail,
                    price,
                    productType,
                    coordinate,
                    providerTranslationAttribution);
        }
    }
}
