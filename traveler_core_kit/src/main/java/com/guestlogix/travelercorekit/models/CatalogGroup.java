package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class CatalogGroup implements Serializable {
    private String title;
    private String subTitle;
    private String description;
    private boolean isFeatured;
    private List<CatalogItem> items;

    private CatalogGroup(String title, String subTitle, String description, boolean isFeatured, List<CatalogItem> items) throws IllegalArgumentException {
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
         * @param reader Object to parse from.
         * @return CatalogGroup model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public CatalogGroup instantiate(JsonReader reader) throws ObjectMappingException {
            String model = "CatalogGroup";
            String key = "CatalogGroup";
            try {
                String title = "";
                String subTitle = "";
                String description = "";
                boolean featured = false;
                List<CatalogItem> items = null;

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
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
            } catch (IllegalStateException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_FIELD, model, key, e.getMessage());
            } catch (JSONException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.MISSING_FIELD, model, key, "");
            } catch (IOException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_DATA,model,key, "IOException has occurred");
            }
        }
    }
}