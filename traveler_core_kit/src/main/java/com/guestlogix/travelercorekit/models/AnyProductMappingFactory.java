package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.util.JsonToken.NULL;

class AnyProductMappingFactory implements ObjectMappingFactory<Product> {
    @Override
    public Product instantiate(JsonReader reader) throws Exception {
        String id = null;
        String title = null;
        Price price = null;
        List<Pass> passes = null;
        Date eventDate = null;
        List<ProductItemCategory> categories = null;
        ProductType productType = null;
        String cancellationPolicy = null;

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
                case "price":
                    price = new Price.PriceObjectMappingFactory().instantiate(reader);
                    break;
                case "cancellationPolicy":
                    cancellationPolicy = reader.nextString();
                    break;
                case "passes":
                    passes = new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()).instantiate(reader);
                    break;
                case "experienceDate":
                    eventDate = DateHelper.parseISO8601(reader.nextString());
                    break;
                case "categories":
                    if (reader.peek() == NULL) {
                        categories = new ArrayList<>();
                        reader.skipValue();
                    } else {
                        categories = JsonReaderHelper.readCatalogItemCategoryArray(reader);
                    }
                    break;
                case "purchaseStrategy":
                    productType = ProductType.fromString(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        Assertion.eval(title != null);
        Assertion.eval(price != null);
        Assertion.eval(passes != null);
        Assertion.eval(productType != null);
        Assertion.eval(categories != null);

        if (productType == ProductType.BOOKABLE) {
            Assertion.eval(eventDate != null);
            return new BookingProduct(id, title, price, passes, eventDate, categories, cancellationPolicy);
        } else {
            throw new RuntimeException("Unknown product type");
        }
    }
}
