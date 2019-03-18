package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
import java.io.Serializable;

public class BookingOption implements Serializable {
    private final String id;
    private String value;

    @SuppressWarnings("ConstantConditions")
    private BookingOption(@NonNull String id, @NonNull String value) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }

        if (value == null) {
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

        @Override
        public BookingOption instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            String id = null;
            String value = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "id":
                        id = JsonReaderHelper.readString(reader);
                        break;
                    case "optionLabel":
                        value = JsonReaderHelper.readString(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            if (id == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "BookingOption 'id' is a required json field"));
            }

            if (value == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "BookingOption 'label' is a required json field"));
            }

            return new BookingOption(id, value);
        }
    }
}
