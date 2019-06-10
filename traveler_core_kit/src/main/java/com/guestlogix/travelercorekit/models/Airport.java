package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class Airport implements Serializable {
    private String code;
    private String name;
    private String city;

    private Airport(String name, String code, String city) {
        this.name = name;
        this.code = code;
        this.city = city;
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
         * @param reader object to parse from.
         * @return Airport model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or missing required field.
         */
        @Override
        public Airport instantiate(JsonReader reader) throws Exception {
            String name = "";
            String code = "";
            String city = "";

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "name":
                        name = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "iata":
                        code = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "city":
                        city = JsonReaderHelper.nextNullableString(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(null != name && !name.trim().isEmpty(), "name can not be empty");
            Assertion.eval(null != code && !code.trim().isEmpty(), "code can not be empty");
            Assertion.eval(null != city && !city.trim().isEmpty(), "city can not be empty");

            return new Airport(name, code, city);
        }
    }
}
