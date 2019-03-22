package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;

public class Attribute implements Serializable {
    private String label;
    private String value;

    public Attribute(String label, String value) throws IllegalArgumentException {

        if (null == label || label.trim().isEmpty()) {
            throw new IllegalArgumentException("label can not be empty");
        } else {
            this.label = label;
        }
        if (null == value || value.trim().isEmpty()) {
            throw new IllegalArgumentException("value can not be empty");
        } else {
            this.value = value;
        }
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    /**
     * Factory class to construct Attribute model from {@code JsonReader}.
     */
    static class AttributeObjectMappingFactory implements ObjectMappingFactory<Attribute> {

        /**
         * Parses a reader object into Attribute model.
         *
         * @param reader Object to parse from.
         * @return Airport model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public Attribute instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "Attribute";
            try {
                String label = "";
                String value = "";

                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "label":
                            label = JsonReaderHelper.readString(reader);
                            break;
                        case "value":
                            value = JsonReaderHelper.readString(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new Attribute(label, value);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }

    }
}
