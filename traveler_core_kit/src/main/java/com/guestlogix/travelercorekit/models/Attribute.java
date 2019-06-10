package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
         * @param reader object to parse from.
         * @return Attribute model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or missing required field.
         */
        @Override
        public Attribute instantiate(JsonReader reader) throws Exception {
            String label = "";
            String value = "";

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "label":
                        label = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "value":
                        value = JsonReaderHelper.nextNullableString(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != label && !label.trim().isEmpty(), "label can not be empty");
            Assertion.eval(null != value && !value.trim().isEmpty(), "value can not be empty");

            return new Attribute(label, value);
        }
    }
}
