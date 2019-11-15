package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;

public class CategoryFacet implements Serializable {
    private BookingItemCategory category;
    private int quantity;

    private CategoryFacet(BookingItemCategory category, int quantity) {
        this.category = category;
        this.quantity = quantity;
    }

    public BookingItemCategory getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    static class CategoryFacetObjectMappingFactory implements ObjectMappingFactory<CategoryFacet> {
        @Override
        public CategoryFacet instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            BookingItemCategory category = BookingItemCategory.fromString(jsonObject.getString("label"));
            int quantity = jsonObject.getInt("count");


            return new CategoryFacet(category, quantity);
        }
    }
}
