package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.Log;

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
    private static final String TAG = "AnyProductMappingFactor";
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

        Assertion.eval(id != null);
        Assertion.eval(title != null);
        Assertion.eval(price != null);
        Assertion.eval(productType != null);
        Assertion.eval(eventDate != null);

        switch (productType) {
            case BOOKABLE:
                return new BookingProduct(id, title, price, passes, eventDate, categories, cancellationPolicy);
            case PARKING:
                return new ParkingProduct(id, title, price, eventDate);
            default:
                Log.e(TAG, "Product (title:" + title + " has unknown product type:" + productType);
                return null;
        }
    }
}
