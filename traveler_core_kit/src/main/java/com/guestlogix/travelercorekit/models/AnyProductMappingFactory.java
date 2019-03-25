package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
import java.util.List;

/**
 * Internal class responsible for mapping payloads to an appropriate product.
 */
class AnyProductMappingFactory implements ObjectMappingFactory<Product> {

    @Override
    public Product instantiate(JsonReader reader) throws ObjectMappingException, IOException {
       /*
            Individual json elements might come back in every order possible. That is why we cannot assume that
            that an product is of type X prior to finding the 'purchaseStrategy' field in the payload. Therefore, we must
            read all field prior to instantiating the correct product.

            When adding new types, just add the extra fields as temporary variables and read the whole object. Then manually
            instantiate your product.
        */
        String key = "Product";
        String id = null;
        Price price = null;
        String purchaseStrategy = null;
        String title = null;
        List<Pass> passes = null;

        reader.beginObject();
        try {

            while (reader.hasNext()) {
                key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.readNonNullString(reader);
                        break;
                    case "price":
                        price = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    case "title":
                        title = JsonReaderHelper.readString(reader);
                        break;
                    case "purchaseStrategy":
                        purchaseStrategy = JsonReaderHelper.readNonNullString(reader);
                        break;
                    case "passes":
                        passes = new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()).instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            if (purchaseStrategy == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "Payload must include a non-null and defined 'purchaseStrategy' field"));
            }

            // Add extra types here.
            if (purchaseStrategy.equalsIgnoreCase("bookable")) {
                return new BookableProduct(id, price, passes, title);
            }
        } catch (IllegalArgumentException e) {
            throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
        }

        throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, purchaseStrategy + " type is not yet supported"));
    }
}
