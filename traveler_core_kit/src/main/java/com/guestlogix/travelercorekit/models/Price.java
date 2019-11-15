package com.guestlogix.travelercorekit.models;

import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Price implements Serializable {
    private double value;
    private Currency baseCurrency;
    private boolean exchangeEnabled;
    private ExchangeRates exchangeRates;

    public static class ExchangeException extends Exception {
        public enum Cause {
            NOT_ALLOWED
        }

        private Cause cause;

        ExchangeException(Cause cause) {
            this.cause = cause;
        }
    }

    Price(double value, Currency currency, boolean exchangeEnabled, ExchangeRates rates) {
        this.value = value;
        this.baseCurrency = currency;
        this.exchangeEnabled = exchangeEnabled;
        this.exchangeRates = rates;
    }

    public static Price zero() {
        return new Price(0, Currency.USD, true, ExchangeRates.equalRates());
    }

    public String getValueWithBaseCurrencySuffix() {
        return (getValueInBaseCurrency() + " " + getBaseCurrencyCode());
    }

    public String getLocalizedDescriptionInBaseCurrency() {
        return getLocalizedDescription(baseCurrency);
    }

    public String getBaseCurrencyCode() {
        return Currency.getCode(baseCurrency);
    }

    public String getLocalizedDescription(Currency currency) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        java.util.Currency nativeCurrency = java.util.Currency.getInstance(Currency.getCode(currency));
        symbols.setCurrencySymbol(nativeCurrency.getSymbol());
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(value);
    }

    public double getValueInBaseCurrency() {
        return value;
    }

    public double getValue(Currency currency) {
        if (currency == baseCurrency) {
            return value;
        }

        if (!exchangeEnabled) {
            return getValueInBaseCurrency();
        }

        return value * exchangeRates.getRate(currency);
    }

    private static @Nullable
    Currency commonCurrency(Price lhs, Price rhs) {
        if (lhs.baseCurrency == rhs.baseCurrency) {
            return lhs.baseCurrency;
        } else if (lhs.exchangeEnabled) {
            return rhs.baseCurrency;
        } else if (rhs.exchangeEnabled) {
            return lhs.baseCurrency;
        } else {
            return null;
        }
    }

    public Price subtract(Price price) throws ExchangeException {
        Currency currency = commonCurrency(this, price);

        if (currency == null) {
            throw new ExchangeException(ExchangeException.Cause.NOT_ALLOWED);
        }

        return new Price(this.getValue(currency) - price.getValue(currency),
                currency,
                this.exchangeEnabled && price.exchangeEnabled,
                this.exchangeRates);
    }

    public Price add(Price price) throws ExchangeException {
        Currency currency = commonCurrency(this, price);

        if (currency == null) {
            throw new ExchangeException(ExchangeException.Cause.NOT_ALLOWED);
        }

        return new Price(this.getValue(currency) + price.getValue(currency),
                currency,
                this.exchangeEnabled && price.exchangeEnabled,
                this.exchangeRates);
    }

    public Price times(Double multiplier) {
        return new Price(this.value * multiplier,
                this.baseCurrency,
                this.exchangeEnabled,
                this.exchangeRates);
    }

    static class PriceObjectMappingFactory implements ObjectMappingFactory<Price> {
        @Override
        public Price instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            double value = jsonObject.getDouble("value");
            Currency baseCurrency = Currency.getInstance(jsonObject.getString("baseCurrency"));
            boolean exchangeEnabled = jsonObject.getBoolean("exchangeEnabled");
            ExchangeRates rates = new ExchangeRates.ExhangeRatesObjectMappingFactory().instantiate(jsonObject.getJSONObject("exchangeRates").toString());

            Assertion.eval(baseCurrency != null);
            Assertion.eval(rates != null);

            return new Price(value, baseCurrency, exchangeEnabled, rates);
        }

    }
}
