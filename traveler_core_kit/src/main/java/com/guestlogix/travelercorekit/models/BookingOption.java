package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;

public class BookingOption implements Serializable {
    private final String id;
    private String value;
    private String disclaimer;

    private BookingOption(@NonNull String id, @NonNull String value, String disclaimer) {
        this.id = id;
        this.value = value;
        this.disclaimer = disclaimer;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    static class BookingOptionObjectMappingFactory implements ObjectMappingFactory<BookingOption> {

        @Override
        public BookingOption instantiate(JsonReader reader) throws Exception {
            String id = null;
            String value = null;
            String disclaimer = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "optionLabel":
                        value = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "disclaimer":
                        disclaimer = JsonReaderHelper.nextNullableString(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(id != null);
            Assertion.eval(value != null);

            return new BookingOption(id, value, disclaimer);
        }
    }
}
