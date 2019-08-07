package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductCancellationQuote implements Serializable {
    private String title;
    private Price totalRefund;
    private Price cancellationCharge;

    ProductCancellationQuote(String title, Price totalRefund, Price cancellationCharge) {
        this.title = title;
        this.totalRefund = totalRefund;
        this.cancellationCharge = cancellationCharge;
    }

    static class ProductCancellationQuoteObjectMappingFactory implements ObjectMappingFactory<ProductCancellationQuote> {
        @Override
        public ProductCancellationQuote instantiate(JsonReader reader) throws Exception {
            String title = null;
            Price totalRefund = null;
            Price cancellationCharge = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "title":
                        title = reader.nextString();
                        break;
                    case "totalRefund":
                        totalRefund = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    case "cancellationCharge":
                        cancellationCharge = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(title != null);
            Assertion.eval(totalRefund != null);
            Assertion.eval(cancellationCharge != null);

            return new ProductCancellationQuote(title, totalRefund, cancellationCharge);
        }
    }
}
