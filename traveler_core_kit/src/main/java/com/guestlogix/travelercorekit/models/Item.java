package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;

public class Item {

    private String id;
    private String title;
    private String subTitle;
    private String thumbnail;
    private Integer vendor;

    public Item(String id, String title, String subTitle, String thumbnail, Integer vendor) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.thumbnail = thumbnail;
        this.vendor = vendor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getVendor() {
        return vendor;
    }

    public void setVendor(Integer vendor) {
        this.vendor = vendor;
    }

    public static class ItemObjectMappingFactory implements ObjectMappingFactory<Item> {

        @Override
        public Item instantiate(JsonReader reader) throws IOException {
            return readItem(reader);
        }

        private Item readItem(JsonReader reader) throws IOException {
            String id = "";
            String title = "";
            String subTitle = "";
            String thumbnail = "";
            Integer vendor = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "id":
                        id = JsonReaderHelper.readString(reader);
                        break;
                    case "title":
                        title = JsonReaderHelper.readString(reader);
                        break;
                    case "subTitle":
                        subTitle = JsonReaderHelper.readString(reader);
                        break;
                    case "thumbnail":
                        thumbnail = JsonReaderHelper.readString(reader);
                        break;
                    case "vendor":
                        vendor = JsonReaderHelper.readInteger(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new Item(id, title, subTitle, thumbnail, vendor);
        }
    }
}