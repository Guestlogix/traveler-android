package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;

import java.io.IOException;
import java.util.List;

public class Group {

    private String title;
    private String subTitle;
    private String description;
    private Boolean featured;
    private List<Item> items;

    public Group(String title, String subTitle, String description, Boolean featured, List<Item> items) {
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.featured = featured;
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

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class GroupObjectMappingFactory implements ObjectMappingFactory<Group> {

        @Override
        public Group instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            return readGroup(reader);
        }

        private static Group readGroup(JsonReader reader) throws IOException, ObjectMappingException {
            String title = "";
            String subTitle = "";
            String description = "";
            Boolean featured = false;
            List<Item> items = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "title":
                        title = reader.nextString();
                        break;
                    case "subTitle":
                        subTitle = reader.nextString();
                        break;
                    case "description":
                        JsonToken peek = reader.peek();
                        if (peek == JsonToken.NULL) {
                            description = null;
                            reader.skipValue();
                        } else {
                            description = reader.nextString();
                        }
                        break;
                    case "featured":
                        featured = reader.nextBoolean();
                        break;
                    case "items":
                        items = new ArrayMappingFactory<>(new Item.ItemObjectMappingFactory()).instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new Group(title, subTitle, description, featured, items);
        }
    }
}