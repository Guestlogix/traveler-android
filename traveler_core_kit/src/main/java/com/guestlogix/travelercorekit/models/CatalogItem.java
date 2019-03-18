package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

public class CatalogItem extends Product implements Serializable {
    private String title;
    private String subTitle;
    private URL imageURL;

    private CatalogItem(String id, String title, String subTitle, URL imageURL) throws IllegalArgumentException {
        super(id, new Price());
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
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
            try {
                String id = "";
                String title = "";
                String subTitle = "";
                URL thumbnail = null;

                reader.beginObject();

                while (reader.hasNext()) {
                    String name = reader.nextName();

                    switch (name) {
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
                            thumbnail = new URL(JsonReaderHelper.readString(reader));
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new CatalogItem(id, title, subTitle, thumbnail);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, e.getMessage()));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}