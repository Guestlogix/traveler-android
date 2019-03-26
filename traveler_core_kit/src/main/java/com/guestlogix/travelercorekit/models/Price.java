package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
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

    /**
     * Factory class to construct Price model from {@code JsonReader}.
     */
    static class PriceObjectMappingFactory implements ObjectMappingFactory<Price> {

        /**
         * Parses a reader object into Price model.
         *
         * @param reader Object to parse from.
         * @return Price model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public Price instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "Price";
            try {
                Double value = 0.0;
                String currency = "";

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "value":
                            value = JsonReaderHelper.readNonNullDouble(reader);
                            break;
                        case "currency":
                            currency = JsonReaderHelper.readNonNullString(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }
                reader.endObject();

                return new Price(value, currency);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }

    }
}
