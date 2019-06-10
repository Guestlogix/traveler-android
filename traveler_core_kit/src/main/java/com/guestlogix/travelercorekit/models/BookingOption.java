package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class BookingOption implements Serializable {
    private final String id;
    private String value;

    @SuppressWarnings("ConstantConditions")
    private BookingOption(String id, String value) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id can not be null");
        }

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value can not be null");
        }

        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    static class BookingOptionObjectMappingFactory implements ObjectMappingFactory<BookingOption> {
        /**
         * Parses a reader object into BookingOption model.
         *
         * @param reader object to parse from.
         * @return BookingOption model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or missing required field.
         */
        @Override
        public BookingOption instantiate(JsonReader reader) throws Exception {
            String id = null;
            String value = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "optionLabel":
                        value = JsonReaderHelper.nextNullableString(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != id && !id.isEmpty(), "id can not be null");
            Assertion.eval(null != value && !value.isEmpty(), "value can not be null");

            return new BookingOption(id, value);
        }
    }
}
