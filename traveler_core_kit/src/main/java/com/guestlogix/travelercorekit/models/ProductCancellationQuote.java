package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;

public class ProductCancellationQuote implements Serializable {
    private String title;
    private Price totalRefund;
    private Price cancellationCharge;

    ProductCancellationQuote(String title, Price totalRefund, Price cancellationCharge) {
        this.title = title;
        this.totalRefund = totalRefund;
        this.cancellationCharge = cancellationCharge;
    }

    public Price getTotalRefund() {
        return totalRefund;
    }

    public Price getCancellationCharge() {
        return cancellationCharge;
    }

    public String getTitle() {
        return title;
    }

    static class ProductCancellationQuoteObjectMappingFactory implements ObjectMappingFactory<ProductCancellationQuote> {
        @Override
        public ProductCancellationQuote instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String title =  jsonObject.getString("title");
            Price totalRefund = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("totalRefund").toString());
            Price cancellationCharge = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("cancellationCharge").toString());

            Assertion.eval(title != null);
            Assertion.eval(totalRefund != null);
            Assertion.eval(cancellationCharge != null);

            return new ProductCancellationQuote(title, totalRefund, cancellationCharge);
        }
    }
}
