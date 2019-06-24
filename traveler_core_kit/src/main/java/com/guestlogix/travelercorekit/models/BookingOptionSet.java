package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


public class BookingOptionSet implements Serializable {
    private String label;
    private List<BookingOption> options;

    private BookingOptionSet(@NonNull String label, @NonNull List<BookingOption> options) {
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

        @Override
        public BookingOptionSet instantiate(JsonReader reader) throws Exception {
            String label = null;
            List<BookingOption> options = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "optionSetLabel":
                        label = reader.nextString();
                        break;
                    case "options":
                        options = new ArrayMappingFactory<>(new BookingOption.BookingOptionObjectMappingFactory()).instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(label != null);
            Assertion.eval(options != null);

            return new BookingOptionSet(label, options);
        }
    }
}
