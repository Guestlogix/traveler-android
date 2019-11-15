package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.*;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Pass implements Serializable {
    private String id;
    private String name;
    private String description;
    private Price price;

    private Pass(@NonNull String id, @NonNull String name, String description, @NonNull Price price) {
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

    public PurchasePass toPurchasePass() {
        return new PurchasePass(id, name, description, price);
    }

    //TODO: this must be fixed. we need to remove passes from purchase form and send purchase form along wih passes to order api instead.
    static List<PurchasePass> toPurchasePassList(List<Pass> passes) {
        List<PurchasePass> purchasePasses = new ArrayList<>();
        for (Pass pass : passes) {
            purchasePasses.add(pass.toPurchasePass());
        }
        return purchasePasses;
    }

    static class PassObjectMappingFactory implements ObjectMappingFactory<Pass> {
        @Override
        public Pass instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String name = jsonObject.getString("title");
            String description = jsonObject.getNullableString("description");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("price").toString());

            Assertion.eval(id != null);
            Assertion.eval(name != null);
            Assertion.eval(price != null);

            return new Pass(id, name, description, price);
        }
    }
}