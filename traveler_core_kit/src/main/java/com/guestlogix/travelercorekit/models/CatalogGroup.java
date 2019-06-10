package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.List;

public class CatalogGroup implements Serializable {
    private String title;
    private String subTitle;
    private String description;
    private Boolean isFeatured;
    private List<CatalogItem> items;

    private CatalogGroup(String title, String subTitle, String description, Boolean isFeatured, List<CatalogItem> items) throws IllegalArgumentException {
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.isFeatured = isFeatured;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    /**
     * Factory class to construct CatalogGroup model from {@code JsonReader}.
     */
    static class GroupObjectMappingFactory implements ObjectMappingFactory<CatalogGroup> {

        /**
         * Parses a reader object into CatalogGroup model.
         *
         * @param reader object to parse from.
         * @return CatalogGroup model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */
        @Override
        public CatalogGroup instantiate(JsonReader reader) throws Exception {
            String title = "";
            String subTitle = "";
            String description = "";
            Boolean featured = false;
            List<CatalogItem> items = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "subTitle":
                        subTitle = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "featured":
                        featured = JsonReaderHelper.nextNullableBoolean(reader);
                        break;
                    case "items":
                        if (JsonToken.NULL != reader.peek()) {
                            items = new ArrayMappingFactory<>(new CatalogItem.CatalogItemObjectMappingFactory()).instantiate(reader);
                        } else {
                            reader.skipValue();
                        }
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new CatalogGroup(title, subTitle, description, featured, items);
        }
    }
}