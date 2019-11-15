package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
import java.util.List;

public class Facets implements Serializable {
    private Price minPrice;
    private Price maxPrice;
    private List<CategoryFacet> categories;

    private Facets(Price minPrice, Price maxPrice, List<CategoryFacet> categories) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.categories = categories;
    }


    public Price getMinPrice() {
        return minPrice;
    }

    public Price getMaxPrice() {
        return maxPrice;
    }

    public List<CategoryFacet> getCategories() {
        return categories;
    }

    static class FacetsObjectMappingFactory implements ObjectMappingFactory<Facets> {
        @Override
        public Facets instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            PriceLimits priceLimits = new PriceLimits.PriceLimitsObjectMappingFactory()
                    .instantiate(jsonObject.getJSONObject("price").toString());
            Price minPrice = priceLimits.getMin();
            Price maxPrice = priceLimits.getMax();

            List<CategoryFacet> categories = new ArrayMappingFactory<>(new CategoryFacet.CategoryFacetObjectMappingFactory()).instantiate(jsonObject.getJSONArray("categories").toString());

            return new Facets(minPrice, maxPrice, categories);
        }
    }
}
