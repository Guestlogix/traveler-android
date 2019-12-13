package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.Assertion;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

//TODO: this model hierarchy makes no sense change this. why do we need to have the same interface for booking passes and product offerigns ??? they are two totally different entity
public class PartnerOffering implements ProductOffering, Serializable {
    private String id;
    private String name;
    private String description;
    private URL iconUrl;
    private Price price;
    private int availableQuantity;

    public PartnerOffering(String id, String name, String description, URL iconUrl, Price price, int availableQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public URL getIconUrl() {
        return iconUrl;
    }

    public Price getPrice() {
        return price;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public static class PartnerOfferingObjectMappingFactory implements com.guestlogix.travelercorekit.utilities.ObjectMappingFactory<PartnerOffering> {
        @Override
        public PartnerOffering instantiate(JsonReader reader) throws Exception {
            String id = null;
            String name = null;
            String description = null;
            String iconUrlString = null;
            Price price = null;
            Integer availableQuantity = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "name":
                        name = reader.nextString();
                        break;
                    case "description":
                        description = reader.nextString();
                        break;
                    case "image":
                        iconUrlString = reader.nextString();
                        break;
                    case "price":
                        price = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    case "availableQuantity":
                        availableQuantity = reader.nextInt();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(id != null);
            Assertion.eval(name != null);
            Assertion.eval(price != null);
            Assertion.eval(availableQuantity != null);

            URL iconUrl = null;
            try {
                iconUrl = new URL(iconUrlString);
            } catch (MalformedURLException e) {
            }

            return new PartnerOffering(id, name, description, iconUrl, price, availableQuantity);
        }

    }
}
