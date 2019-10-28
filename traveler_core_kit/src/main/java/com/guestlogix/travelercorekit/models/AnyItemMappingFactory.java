package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnyItemMappingFactory implements ObjectMappingFactory<CatalogItem> {
    @Override
    public CatalogItem instantiate(JsonReader reader) throws Exception {
        String id = null;
        String title = null;
        String subTitle = null;
        URL thumbnail = null;
        Price price = null;
        List<ProductItemCategory> categories = null;
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
        Assertion.eval(productType != null);

        switch (productType) {
            case BOOKABLE:
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
            case PARKING:
                return new ParkingItem(
                        id,
                        title,
                        subTitle,
                        thumbnail,
                        price,
                        productType,
                        categories,
                        coordinate,
                        providerTranslationAttribution);
            default:
                throw new RuntimeException("Unknown product type");
        }
    }
}
