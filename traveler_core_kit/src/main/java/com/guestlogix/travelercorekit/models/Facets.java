package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
        public Facets instantiate(JsonReader reader) throws Exception {
            PriceLimits priceLimits = null;
            Price minPrice = null;
            Price maxPrice = null;
            List<CategoryFacet> categories = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "price":
                        priceLimits = new PriceLimits.PriceLimitsObjectMappingFactory()
                                .instantiate(reader);
                        minPrice = priceLimits.getMin();
                        maxPrice = priceLimits.getMax();
                        break;
                    case "categories":
                        categories = new ArrayMappingFactory<>(new CategoryFacet.CategoryFacetObjectMappingFactory()).instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            return new Facets(minPrice, maxPrice, categories);
        }
    }
}
