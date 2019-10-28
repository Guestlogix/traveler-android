package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class CategoryFacet implements Serializable {
    private ProductItemCategory category;
    private int quantity;

    private CategoryFacet(ProductItemCategory category, int quantity) {
        this.category = category;
        this.quantity = quantity;
    }

    public ProductItemCategory getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    static class CategoryFacetObjectMappingFactory implements ObjectMappingFactory<CategoryFacet> {
        @Override
        public CategoryFacet instantiate(JsonReader reader) throws Exception {
            ProductItemCategory category = null;
            int quantity = 0;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "label":
                        category = ProductItemCategory.fromString(JsonReaderHelper.nextNullableString(reader));
                        break;
                    case "count":
                        quantity = reader.nextInt();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            return new CategoryFacet(category, quantity);
        }
    }
}
