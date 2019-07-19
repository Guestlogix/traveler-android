package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
        public ExchangeRates instantiate(JsonReader reader) throws Exception {
            double usd = 0;
            double cad = 0;
            double aud = 0;
            double eur = 0;
            double gbp = 0;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "usd":
                        usd = reader.nextDouble();
                        break;
                    case "cad":
                        cad = reader.nextDouble();
                        break;
                    case "aud":
                        aud = reader.nextDouble();
                        break;
                    case "eur":
                        eur = reader.nextDouble();
                        break;
                    case "gbp":
                        gbp = reader.nextDouble();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(usd != 0);
            Assertion.eval(cad != 0);
            Assertion.eval(aud != 0);
            Assertion.eval(eur != 0);
            Assertion.eval(gbp != 0);

            return new ExchangeRates(usd, cad, aud, eur, gbp);
        }
    }
}
