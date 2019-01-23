package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.util.List;

public class CatalogGroup {

    private String title;
    private String subTitle;
    private String description;
    private Boolean isFeatured;
    private List<CatalogItem> items;

    public CatalogGroup(String title, String subTitle, String description, Boolean isFeatured, List<CatalogItem> items) {
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.isFeatured = isFeatured;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    public void setItems(List<CatalogItem> items) {
        this.items = items;
    }

    public static class GroupObjectMappingFactory implements ObjectMappingFactory<CatalogGroup> {

        @Override
        public CatalogGroup instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            return readGroup(reader);
        }

        private static CatalogGroup readGroup(JsonReader reader) throws IOException, ObjectMappingException {
            String title = "";
            String subTitle = "";
            String description = "";
            Boolean featured = false;
            List<CatalogItem> items = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "title":
                        title = JsonReaderHelper.readString(reader);
                        break;
                    case "subTitle":
                        subTitle = JsonReaderHelper.readString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.readString(reader);
                        break;
                    case "featured":
                        featured = JsonReaderHelper.readBoolean(reader);
                        break;
                    case "items":
                        items = new ArrayMappingFactory<>(new CatalogItem.CatalogItemObjectMappingFactory()).instantiate(reader);
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