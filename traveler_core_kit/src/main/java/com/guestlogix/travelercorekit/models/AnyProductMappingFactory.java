package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.*;

import java.util.List;

/**
 * Internal class responsible for mapping payloads to an appropriate product.
 */
class AnyProductMappingFactory implements ObjectMappingFactory<Product> {

    @Override
    public Product instantiate(JsonReader reader) throws Exception {
       /*
            Individual json elements might come back in every order possible. That is why we cannot assume that
            that an product is of type X prior to finding the 'purchaseStrategy' field in the payload. Therefore, we must
            read all field prior to instantiating the correct product.

            When adding new types, just add the extra fields as temporary variables and read the whole object. Then manually
            instantiate your product.
        */
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
                    id = JsonReaderHelper.nextNullableString(reader);
                    break;
                case "price":
                    if (JsonToken.NULL != reader.peek()) {
                        price = new Price.PriceObjectMappingFactory().instantiate(reader);
                    } else {
                        price = null;
                        reader.skipValue();
                    }
                    break;
                case "title":
                    title = JsonReaderHelper.nextNullableString(reader);
                    break;
                case "purchaseStrategy":
                    purchaseStrategy = JsonReaderHelper.nextNullableString(reader);
                    break;
                case "passes":
                    if (JsonToken.NULL != reader.peek()) {
                        passes = new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()).instantiate(reader);
                    } else {
                        reader.skipValue();
                    }
                    break;
                default:
                    reader.skipValue();
            }
        }

        reader.endObject();

        Assertion.eval((purchaseStrategy != null));
        try {
            Assertion.eval(purchaseStrategy.equalsIgnoreCase("bookable"));
        } catch (AssertionException e) {
            throw new IllegalArgumentException(purchaseStrategy + " type is not yet supported");
        }

        // Add extra types here.
        return new BookableProduct(id, price, passes, title);

    }
}
