package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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
        public PriceLimits instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            Price max = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("max").toString());
            Price min = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("min").toString());

            Assertion.eval(max != null);
            Assertion.eval(min != null);

            return new PriceLimits(max, min);
        }
    }
}
