package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


public class BookingOptionSet implements Serializable {
    private String label;
    private List<BookingOption> options;

    @SuppressWarnings("ConstantConditions")
    private BookingOptionSet(@NonNull String label, @NonNull List<BookingOption> options) {
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

        @Override
        public BookingOptionSet instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            String label = null;
            List<BookingOption> options = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "optionSetlabel":
                        label = JsonReaderHelper.readString(reader);
                        break;
                    case "options":
                        options = new ArrayMappingFactory<>(new BookingOption.BookingOptionObjectMappingFactory()).instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            if (label == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "BookingOptionSet 'label' is a required json field"));
            }

            if (options == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "BookingOptionSet 'options' is a required json field"));
            }

            return new BookingOptionSet(label, options);
        }
    }
}
