package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
         * @param reader object to parse from.
         * @return Choice model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or if the required field is missing.
         */
        @Override
        public Choice instantiate(JsonReader reader) throws Exception {
            String id = "";
            String value = "";

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    //key changed in api for product questions: optionLabel -> label
                    case "label":
                        value = JsonReaderHelper.nextNullableString(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(id != null && !id.trim().isEmpty(), "id cannot be empty");

            return new Choice(id, value);
        }
    }
}
