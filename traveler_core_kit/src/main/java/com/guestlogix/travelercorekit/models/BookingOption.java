package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;

public class BookingOption implements Serializable {
    private final String id;
    private String value;

    @SuppressWarnings("ConstantConditions")
    private BookingOption(String id, String value) {
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
        public BookingOption instantiate(JsonReader reader) throws ObjectMappingException {
            String model = "BookingOption";
            String key = "BookingOption";

            try {
                String id = null;
                String value = null;

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "id":
                            id = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "optionLabel":
                            value = JsonReaderHelper.readNonNullString(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new BookingOption(id, value);
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
