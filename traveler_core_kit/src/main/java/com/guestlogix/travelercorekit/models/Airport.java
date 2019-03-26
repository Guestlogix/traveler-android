package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;

public class Airport implements Serializable {
    private String code;
    private String name;
    private String city;

    private Airport(String name, String code, String city) throws IllegalArgumentException {

        if (null == name || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        } else {
            this.name = name;
        }
        if (null == code || code.trim().isEmpty()) {
            throw new IllegalArgumentException("code can not be empty");
        } else {
            this.code = code;
        }
        if (null == city || city.trim().isEmpty()) {
            throw new IllegalArgumentException("city can not be empty");
        } else {
            this.city = city;
        }
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCity() {
        return city;
    }

    /**
     * Factory class to construct Airport model from {@code JsonReader}.
     */
    static class AirportObjectMappingFactory implements ObjectMappingFactory<Airport> {

        /**
         * Parses a reader object into Airport model.
         *
         * @param reader Object to parse from.
         * @return Airport model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public Airport instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "Airport";
            try {
                String name = "";
                String code = "";
                String city = "";

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "name":
                            name = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "iata":
                            code = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "city":
                            city = JsonReaderHelper.readNonNullString(reader);
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }

                reader.endObject();
                return new Airport(name, code, city);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
