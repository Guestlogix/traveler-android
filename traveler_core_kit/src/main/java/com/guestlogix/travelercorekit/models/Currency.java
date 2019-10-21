package com.guestlogix.travelercorekit.models;

public enum Currency {
    USD, CAD, AUD, EUR, GBP;

    public static class UnknownCurrencyException extends Exception {
        private String code;

        UnknownCurrencyException(String code) {
            this.code = code;
        }
    }

    static Currency getInstance(String code) throws UnknownCurrencyException {
        switch (code) {
            case "USD":
                return USD;
            case "CAD":
                return CAD;
            case "AUD":
                return AUD;
            case "EUR":
                return EUR;
            case "GBP":
                return GBP;
            default:
                throw new Currency.UnknownCurrencyException(code);
        }
    }

    public static String getCode(Currency currency) {
        switch (currency) {
            case USD:
                return "USD";
            case EUR:
                return "EUR";
            case CAD:
                return "CAD";
            case GBP:
                return "GBP";
            case AUD:
                return "AUD";
            default:
                // This will never happen
                return null;
        }
    }
}
