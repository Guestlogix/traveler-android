package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;

public class ParkingItem implements CatalogItem<ParkingProduct> {
    private String title;
    private String subTitle;
    private URL imageURL;
    private ParkingProduct parkingProduct;

    ParkingItem(
            String title,
            String subTitle,
            URL imageURL,
            ParkingProduct parkingProduct) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
        this.parkingProduct = parkingProduct;
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
    public ParkingProduct getItemResource() {
        return parkingProduct;
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
                    title,
                    subTitle,
                    thumbnail,
                    new ParkingProduct(id, title, price, productType, coordinate, providerTranslationAttribution));
        }
    }
}
