package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;


public class PurchasePass implements Serializable {
    private String id;
    private String name;
    private String description;
    private Price price;

    PurchasePass(@NonNull String id, @NonNull String name, String description, @NonNull Price price) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    static class PassObjectMappingFactory implements ObjectMappingFactory<PurchasePass> {
        @Override
        public PurchasePass instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String name = jsonObject.getString("title");
            String description = jsonObject.getNullableString("description");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("price").toString());

            Assertion.eval(id != null);
            Assertion.eval(name != null);
            Assertion.eval(price != null);

            return new PurchasePass(id, name, description, price);
        }
    }
}