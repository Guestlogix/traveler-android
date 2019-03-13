package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

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
            try {
                String id = "";
                String value = "";

                reader.beginObject();

                while (reader.hasNext()) {
                    String name = reader.nextName();

                    switch (name) {
                        case "id":
                            id = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "name":
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
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, e.getMessage()));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
