package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

public class CatalogItem extends Product implements Serializable {

    private String title;
    private String subTitle;
    private URL imageURL;

    public CatalogItem(String id, String title, String subTitle, URL imageURL) {
        super(id,0);
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
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

    public URL getImageURL() {
        return imageURL;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
    }

    public static class CatalogItemObjectMappingFactory implements ObjectMappingFactory<CatalogItem> {

        @Override
        public CatalogItem instantiate(JsonReader reader) throws IOException {
            return readItem(reader);
        }

        private CatalogItem readItem(JsonReader reader) throws IOException {
            String id = "";
            String title = "";
            String subTitle = "";
            URL thumbnail = null;

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
                        thumbnail = new URL(JsonReaderHelper.readString(reader));
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new CatalogItem(id, title, subTitle, thumbnail);
        }
    }
}