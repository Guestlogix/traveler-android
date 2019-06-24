package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.*;
import android.util.JsonToken;


import java.util.List;

class AnyProductMappingFactory implements ObjectMappingFactory<Product> {
    @Override
    public Product instantiate(JsonReader reader) throws Exception {
        String id = null;
        Price price = null;
        String purchaseStrategy = null;
        String title = null;
        List<Pass> passes = null;

        reader.beginObject();

        while (reader.hasNext()) {
            String key = reader.nextName();

            switch (key) {
                case "id":
                    id = reader.nextString();
                    break;
                case "price":
                    price = new Price.PriceObjectMappingFactory().instantiate(reader);
                    break;
                case "title":
                    title = JsonReaderHelper.nextNullableString(reader);
                    break;
                case "purchaseStrategy":
                    purchaseStrategy = reader.nextString();
                    break;
                case "passes":
                    passes = new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()).instantiate(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        Assertion.eval(purchaseStrategy != null);
        Assertion.eval(price != null);
        Assertion.eval(passes != null);
        Assertion.eval(title != null);

        if (purchaseStrategy.equalsIgnoreCase("bookable")) {
            return new BookableProduct(id, price, passes, title);
        } else {
            throw new RuntimeException("Unknown product type");
        }
    }
}
