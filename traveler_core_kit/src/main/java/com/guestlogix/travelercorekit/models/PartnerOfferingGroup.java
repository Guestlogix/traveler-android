package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
import java.util.List;

public class PartnerOfferingGroup implements Serializable {
    private String title;
    private String subtitle;
    private List<PartnerOffering> offerings;
    private Price priceStartingAt;

    PartnerOfferingGroup(String title, String subtitle, List<PartnerOffering> offerings, Price priceStartingAt) {
        this.title = title;
        this.subtitle = subtitle;
        this.offerings = offerings;
        this.priceStartingAt = priceStartingAt;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public List<PartnerOffering> getOfferings() {
        return offerings;
    }

    public Price getPriceStartingAt() {
        return priceStartingAt;
    }

    static class PartnerOfferingGroupObjectMappingFactory implements ObjectMappingFactory<PartnerOfferingGroup> {
        @Override
        public PartnerOfferingGroup instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String title = jsonObject.getString("title");
            String subtitle = jsonObject.getString("subTitle");
            List<PartnerOffering> offerings = new ArrayMappingFactory<PartnerOffering>(new PartnerOffering.PartnerOfferingObjectMappingFactory()).instantiate(jsonObject.getJSONArray("items").toString());
            Price priceStartingAt = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("priceStartingAt").toString());

            Assertion.eval(title != null);
            Assertion.eval(subtitle != null);
            Assertion.eval(offerings != null);
            Assertion.eval(priceStartingAt != null);

            return new PartnerOfferingGroup(title, subtitle, offerings, priceStartingAt);
        }
    }
}
