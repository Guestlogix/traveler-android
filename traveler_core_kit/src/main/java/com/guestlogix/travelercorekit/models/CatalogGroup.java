package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class CatalogGroup implements Serializable {
    private String title;
    private String subTitle;
    private boolean isFeatured;
    private List<CatalogItem> items;

    private CatalogGroup(String title, String subTitle, boolean isFeatured, @NonNull List<CatalogItem> items) {
        this.title = title;
        this.subTitle = subTitle;
        this.isFeatured = isFeatured;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() { return subTitle; }

    public boolean isFeatured() { return isFeatured; }

    public List<CatalogItem> getItems() {
        return items;
    }

    static class GroupObjectMappingFactory implements ObjectMappingFactory<CatalogGroup> {
        @Override
        public CatalogGroup instantiate(JsonReader reader) throws Exception {
            String title = null;
            String subTitle = null;
            boolean featured = false;
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
                    case "featured":
                        featured = reader.nextBoolean();
                        break;
                    case "items":
                        items = new ArrayMappingFactory<>(new CatalogItem.CatalogItemObjectMappingFactory()).instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(items != null);

            return new CatalogGroup(title, subTitle, featured, items);
        }
    }
}