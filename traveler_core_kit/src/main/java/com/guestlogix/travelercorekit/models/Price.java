package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Price implements Serializable {
    private Double value;
    private String currency;

    public Price() {
        value = 0.0;
        currency = "USD"; //default currency
    }

    public Price(Double value, String currency) {
        if (value == null) {
            throw new IllegalArgumentException("value can not be null");
        } else {
            this.value = value;
        }

        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("currency can not be empty");
        } else {
            this.currency = currency;
        }
    }

    public Double getValue() {
        return value;
    }

    private String getCurrencySymbol() {
        Currency currency = Currency.getInstance(this.currency);
        return currency.getSymbol(Locale.getDefault());
    }

    public String getCurrency() {
        return currency;
    }

    public String getFormattedValue() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(getCurrencySymbol());
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(value);
    }

    static class PriceObjectMappingFactory implements ObjectMappingFactory<Price> {
        @Override
        public Price instantiate(JsonReader reader) throws Exception {
            double value = 0;
            String currency = null;


            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "value":
                        value = reader.nextDouble();
                        break;
                    case "currency":
                        currency = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            return new Price(value, currency);
        }

    }
}
