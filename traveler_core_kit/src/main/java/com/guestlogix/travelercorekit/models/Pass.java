package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;


public class Pass implements Serializable {
    private String id;
    private String name;
    private String description;
    private Price price;

    private Pass(String id, String name, String description, Price price) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be empty");
        } else {
            this.id = id;
        }
        if (price == null) {
            throw new IllegalArgumentException("price can not be empty");
        } else {
            this.price = price;
        }
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    /**
     * Factory class to construct Pass model from {@code JsonReader}.
     */
    static class PassObjectMappingFactory implements ObjectMappingFactory<Pass> {

        /**
         * Parses a json reader object into a Pass model.
         *
         * @param reader to read from.
         * @return Pass model.
         * @throws ObjectMappingException if mapping fails or any required field is missing.
         */
        @Override
        public Pass instantiate(JsonReader reader) throws ObjectMappingException {
            String model = "Pass";
            String key = "Pass";
            try {
                String id = "";
                String name = "";
                String description = "";
                Price price = null;

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
                            name = JsonReaderHelper.readString(reader);
                            break;
                        case "description":
                            description = JsonReaderHelper.readString(reader);
                            break;
                        case "price":
                            ObjectMappingFactory<Price> p = new Price.PriceObjectMappingFactory();
                            price = p.instantiate(reader);
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }

                reader.endObject();
                return new Pass(id, name, description, price);
            } catch (IllegalStateException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_FIELD, model, key, e.getMessage());
            } catch (JSONException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.MISSING_FIELD, model, key, "");
            } catch (IOException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_DATA, model, key, "IOException has occurred");
            }
        }
    }
}