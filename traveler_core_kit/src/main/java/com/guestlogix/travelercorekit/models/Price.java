package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;

public class Price implements Serializable {

    Double value;
    String currency;

    public Price() {
    }

    public Price(Double value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public static class PriceObjectMappingFactory implements ObjectMappingFactory<Price> {

        @Override
        public Price instantiate(JsonReader reader) throws IOException {
            return readItem(reader);
        }

        private Price readItem(JsonReader reader) throws IOException {
            Double value = 0.0;
            String currency = "";

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "value":
                        value = JsonReaderHelper.readDouble(reader);
                        break;
                    case "currency":
                        currency = JsonReaderHelper.readString(reader);
                        break;
                }
            }
            reader.endObject();
            return new Price(value, currency);
        }
    }
}
