package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

public class PriceLimits {
    private Price max;
    private Price min;

    private PriceLimits(Price max, Price min) {
        this.max = max;
        this.min = min;
    }


    public Price getMax() {
        return max;
    }

    public Price getMin() {
        return min;
    }

    static class PriceLimitsObjectMappingFactory implements ObjectMappingFactory<PriceLimits> {

        @Override
        public PriceLimits instantiate(JsonReader reader) throws Exception {
            Price max = null;
            Price min = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "max":
                        max = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    case "min":
                        min = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(max != null);
            Assertion.eval(min != null);

            return new PriceLimits(max, min);
        }
    }
}
