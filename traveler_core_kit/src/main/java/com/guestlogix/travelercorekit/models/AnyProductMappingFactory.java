package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.*;
import android.util.JsonToken;


import java.util.Date;
import java.util.List;

class AnyProductMappingFactory implements ObjectMappingFactory<Product> {
    @Override
    public Product instantiate(JsonReader reader) throws Exception {
        String id = null;
        Price price = null;
        String purchaseStrategy = null;
        String title = null;
        List<Pass> passes = null;
        Date eventDate = null;

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
                case "experienceDate":
                    eventDate = DateHelper.parseISO8601(reader.nextString());
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
            Assertion.eval(eventDate != null);

            return new BookableProduct(id, price, passes, title, eventDate);
        } else {
            throw new RuntimeException("Unknown product type");
        }
    }
}
