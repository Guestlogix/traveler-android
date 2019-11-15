package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PartnerOffering implements Serializable {
    private String id;
    private String name;
    private String description;
    private URL iconUrl;
    private Price price;
    private int availableQuantity;

    PartnerOffering(String id, String name, String description, URL iconUrl, Price price, int availableQuantity) {
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

    public PurchasePass toPurchasePass() {
        return new PurchasePass(id, name, description, price);
    }

    //TODO: this must be fixed. we need to remove passes from purchase form and send purchase form along wih passes to order api instead.
    static List<PurchasePass> toPurchasePassList(List<PartnerOffering> partnerOfferings) {
        List<PurchasePass> purchasePasses = new ArrayList<>();
        for (PartnerOffering partnerOffering : partnerOfferings) {
            purchasePasses.add(partnerOffering.toPurchasePass());
        }
        return purchasePasses;
    }

    public static class PartnerOfferingObjectMappingFactory implements com.guestlogix.travelercorekit.utilities.ObjectMappingFactory<PartnerOffering> {
        @Override
        public PartnerOffering instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String description = jsonObject.getString("description");
            String iconUrlString = jsonObject.getString("image");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("price").toString());
            Integer availableQuantity = jsonObject.getInt("availableQuantity");

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
