package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
        public CategoryFacet instantiate(JsonReader reader) throws Exception {
            BookingItemCategory category = null;
            int quantity = 0;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "label":
                        category = BookingItemCategory.fromString(JsonReaderHelper.nextNullableString(reader));
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
