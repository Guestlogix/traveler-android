package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.List;

public class PartnerOfferingGroup implements Serializable {
    private String title;
    private String subtitle;
    private List<PartnerOffering> offerings;
    private Price priceStartingAt;

    public PartnerOfferingGroup(String title, String subtitle, List<PartnerOffering> offerings, Price priceStartingAt) {
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
        public PartnerOfferingGroup instantiate(JsonReader reader) throws Exception {
            String title = null;
            String subtitle = null;
            List<PartnerOffering> offerings = null;
            Price priceStartingAt = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "title":
                        title = reader.nextString();
                        break;
                    case "subTitle":
                        subtitle = reader.nextString();
                        break;
                    case "items":
                        offerings = new ArrayMappingFactory<PartnerOffering>(new PartnerOffering.PartnerOfferingObjectMappingFactory()).instantiate(reader);
                        break;
                    case "priceStartingAt":
                        priceStartingAt = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;

                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(title != null);
            Assertion.eval(subtitle != null);
            Assertion.eval(offerings != null);
            Assertion.eval(priceStartingAt != null);

            return new PartnerOfferingGroup(title, subtitle, offerings, priceStartingAt);
        }
    }
}
