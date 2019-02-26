package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

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

    private Price(Double value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    private String getCurrencySymbol() {
        Currency currency = Currency.getInstance(this.currency);
        return currency.getSymbol(Locale.getDefault());
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public String getFormattedValue() {
        return getFormattedValue(value);
    }

    String getFormattedValue(Double price) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(getCurrencySymbol());
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(price);
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
                    default:
                        reader.skipValue();
                }
            }
            reader.endObject();

            return new Price(value, currency);
        }
    }
}
