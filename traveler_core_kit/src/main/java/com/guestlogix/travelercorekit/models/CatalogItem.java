package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class CatalogItem implements Product {
    private String id;
    private String title;
    private String subTitle;
    private URL imageURL;
    private Price price;

    private CatalogItem(String id, String title, String subTitle, URL imageURL) throws IllegalArgumentException {
        if (null == id || id.isEmpty()) {
            throw new IllegalArgumentException("id can not be null");
        } else {
            this.id = id;
        }

        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
        price = new Price();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Price getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public URL getImageURL() {
        return imageURL;
    }

    /**
     * Factory class to construct CatalogItem model from {@code JsonReader}.
     */
    static class CatalogItemObjectMappingFactory implements ObjectMappingFactory<CatalogItem> {
        /**
         * Parses a reader object into CatalogItem model.
         *
         * @param reader object to parse from.
         * @return CatalogItem model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or missing required field.
         */
        @Override
        public CatalogItem instantiate(JsonReader reader) throws Exception {
            String id = "";
            String title = "";
            String subTitle = "";
            URL thumbnail = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "subTitle":
                        subTitle = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "thumbnail":
                        try {
                            thumbnail = new URL(reader.nextString());
                        } catch (MalformedURLException e) {
                            thumbnail = null;
                        }
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != id && !id.isEmpty(), "id can not be empty");

            return new CatalogItem(id, title, subTitle, thumbnail);
        }
    }
}