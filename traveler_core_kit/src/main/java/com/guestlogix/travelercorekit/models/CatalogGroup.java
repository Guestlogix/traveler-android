package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.List;

public class CatalogGroup implements Serializable {
    private String title;
    private String subTitle;
    private boolean isFeatured;
    private CatalogItemType itemType;
    private List<CatalogItem> items;

    private CatalogGroup(
            String title,
            String subTitle,
            boolean isFeatured,
            CatalogItemType itemType,
            @NonNull List<CatalogItem> items) {
        this.title = title;
        this.subTitle = subTitle;
        this.isFeatured = isFeatured;
        this.itemType = itemType;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public CatalogItemType getItemType() {
        return itemType;
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    static class GroupObjectMappingFactory implements ObjectMappingFactory<CatalogGroup> {
        @Override
        public CatalogGroup instantiate(JsonReader reader) throws Exception {
            String title = null;
            String subTitle = null;
            boolean featured = false;
            CatalogItemType itemType = null;
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
                    case "type":
                        itemType = CatalogItemType.fromString(JsonReaderHelper.nextNullableString(reader));
                        break;
                    case "items":
                        Assertion.eval(itemType != null);
                        switch (itemType) {
                            case ITEM:
                                items = new ArrayMappingFactory<>(new AnyItemMappingFactory())
                                        .instantiate(reader);
                                break;
                            case QUERY:
                                items = new ArrayMappingFactory<>(new QueryItem.QueryItemMappingFactory())
                                        .instantiate(reader);
                                break;
                        }
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(items != null);

            return new CatalogGroup(title, subTitle, featured, itemType, items);
        }
    }
}