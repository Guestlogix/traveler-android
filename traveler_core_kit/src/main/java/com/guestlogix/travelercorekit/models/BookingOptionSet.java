package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.List;


public class BookingOptionSet implements Serializable {
    private String label;
    private List<BookingOption> options;

    @SuppressWarnings("ConstantConditions")
    private BookingOptionSet(String label, List<BookingOption> options) {
        if (label == null || label.isEmpty()) {
            throw new IllegalArgumentException("label can not be null or empty");
        }

        if (options == null) {
            throw new IllegalArgumentException("options cannot be null");
        }
        this.label = label;
        this.options = options;
    }

    public String getLabel() {
        return label;
    }

    public List<BookingOption> getOptions() {
        return options;
    }

    static class BookingOptionSetObjectMappingFactory implements ObjectMappingFactory<BookingOptionSet> {
        /**
         * Parses a reader Object into BookingOptionSet model.
         *
         * @param {@code reader} object to parse from.
         * @return {@link BookingOptionSet} model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */
        @Override
        public BookingOptionSet instantiate(JsonReader reader) throws Exception {
            String label = null;
            List<BookingOption> options = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "optionSetLabel":
                        label = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "options":
                        if (JsonToken.NULL != reader.peek()) {
                            options = new ArrayMappingFactory<>(new BookingOption.BookingOptionObjectMappingFactory()).instantiate(reader);
                        } else {
                            options = null;
                            reader.skipValue();
                        }
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != label && !label.isEmpty(), "optionSetLabel can not be empty");
            Assertion.eval(null != options, "options cannot be null");

            return new BookingOptionSet(label, options);
        }
    }
}
