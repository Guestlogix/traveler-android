package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;

public class Airport implements Serializable {
    private String code;
    private String name;
    private String city;

    public Airport(String name, String code, String city) {
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

    public static class AirportObjectMappingFactory implements ObjectMappingFactory<Airport> {
        @Override
        public Airport instantiate(JsonReader reader) throws ObjectMappingException {
            try {
                return readAirport(reader);
            } catch (IOException e) {
                throw new ObjectMappingException(new TravelerError(TravelerErrorCode.PARSING_ERROR, "IOException has occurred"));
            }
        }

        private Airport readAirport (JsonReader reader) throws IOException {
            String name = "";
            String code = "";
            String city = "";

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "name":
                        name = JsonReaderHelper.readString(reader);
                        break;
                    case "iata":
                        code = JsonReaderHelper.readString(reader);
                        break;
                    case "city":
                        city = JsonReaderHelper.readString(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();
            return new Airport(name, code, city);
        }
    }
}
