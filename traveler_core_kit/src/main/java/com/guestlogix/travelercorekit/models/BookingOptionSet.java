package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import org.json.JSONException;

import java.io.IOException;
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

        @Override
        public BookingOptionSet instantiate(JsonReader reader) throws ObjectMappingException {
            String model = "BookingOptionSet";
            String key = "BookingOptionSet";
            try {
                String label = null;
                List<BookingOption> options = null;

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "optionSetLabel":
                            label = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "options":
                            options = new ArrayMappingFactory<>(new BookingOption.BookingOptionObjectMappingFactory()).instantiate(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new BookingOptionSet(label, options);
            } catch (IllegalStateException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_FIELD, model, key, e.getMessage());
            } catch (JSONException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.MISSING_FIELD, model, key, "");
            } catch (IOException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_DATA, model, key, "IOException has occurred");
            }
        }
    }
}
