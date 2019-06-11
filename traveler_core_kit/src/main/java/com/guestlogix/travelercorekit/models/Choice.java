package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;

public class Choice implements Serializable {
    private String id;
    private String value;

    Choice(String id, String value) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be empty");
        } else {
            this.id = id;
        }
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    /**
     * Factory class to construct Choice model from {@code JsonReader}.
     */
    static class ChoiceObjectMappingFactory implements ObjectMappingFactory<Choice> {

        /**
         * Parses a reader object into Choice model.
         *
         * @param reader Object to parse from.
         * @return Choice model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public Choice instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "Choice";
            try {
                String id = "";
                String value = "";

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
                            //key changed in api for product questions: optionLabel -> label
                        case "label":
                            value = JsonReaderHelper.readString(reader);
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }

                reader.endObject();
                return new Choice(id, value);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
