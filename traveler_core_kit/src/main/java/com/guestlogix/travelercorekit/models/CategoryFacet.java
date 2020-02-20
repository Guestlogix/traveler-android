package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class CategoryFacet implements Serializable {
    //TODO replace String type to BookingItemCategory when backend is ready.
    private String category;
    private int quantity;

    private CategoryFacet(String category, int quantity) {
        this.category = category;
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    static class CategoryFacetObjectMappingFactory implements ObjectMappingFactory<CategoryFacet> {
        @Override
        public CategoryFacet instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String category = jsonObject.getString("label");
            int quantity = jsonObject.getInt("count");


            return new CategoryFacet(category, quantity);
        }
    }
}
