package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CatalogItem implements Product, Serializable {
    private String id;
    private String title;
    private String subTitle;
    private URL imageURL;
    private Price price;

    private CatalogItem(String id, String title, String subTitle, URL imageURL) throws IllegalArgumentException {
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
         * @param reader Object to parse from.
         * @return CatalogItem model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public CatalogItem instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "CatalogItem";
            try {
                String id = "";
                String title = "";
                String subTitle = "";
                URL thumbnail = null;

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "id":
                            id = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "title":
                            title = JsonReaderHelper.readString(reader);
                            break;
                        case "subTitle":
                            subTitle = JsonReaderHelper.readString(reader);
                            break;
                        case "thumbnail":
                            try {
                                thumbnail = new URL(JsonReaderHelper.readString(reader));
                            } catch (MalformedURLException e) {
                                thumbnail = null;
                            }
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new CatalogItem(id, title, subTitle, thumbnail);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}