package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;

class ExchangeRates implements Serializable {
    private double usd;
    private double cad;
    private double aud;
    private double eur;
    private double gbp;

    private ExchangeRates(double usd, double cad, double aud, double eur, double gbp) {
        this.usd = usd;
        this.cad = cad;
        this.aud = aud;
        this.eur = eur;
        this.gbp = gbp;
    }

    double getRate(Currency currency) {
        switch (currency) {
            case USD:
                return usd;
            case CAD:
                return cad;
            case AUD:
                return aud;
            case EUR:
                return eur;
            case GBP:
                return gbp;
            default:
                return 1;
        }
    }

    static ExchangeRates equalRates() {
        return new ExchangeRates(1,1,1,1,1);
    }

    static class ExhangeRatesObjectMappingFactory implements ObjectMappingFactory<ExchangeRates> {
        @Override
        public ExchangeRates instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            double usd = jsonObject.getDouble("usd");
            double cad = jsonObject.getDouble("cad");
            double aud = jsonObject.getDouble("aud");
            double eur = jsonObject.getDouble("eur");
            double gbp = jsonObject.getDouble("gbp");

            Assertion.eval(usd != 0);
            Assertion.eval(cad != 0);
            Assertion.eval(aud != 0);
            Assertion.eval(eur != 0);
            Assertion.eval(gbp != 0);

            return new ExchangeRates(usd, cad, aud, eur, gbp);
        }
    }
}
