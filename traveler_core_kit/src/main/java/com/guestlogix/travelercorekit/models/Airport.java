package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;
import java.io.Serializable;

public class Airport implements Serializable {
    private String code;
    private String name;
    private String city;

    private Airport(@NonNull String name, @NonNull String code, @NonNull String city) {
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

    static class AirportObjectMappingFactory implements ObjectMappingFactory<Airport> {
        @Override
        public Airport instantiate(JsonReader reader) throws Exception {
            String name = null;
            String code = null;
            String city = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "name":
                        name = reader.nextString();
                        break;
                    case "iata":
                        code = reader.nextString();
                        break;
                    case "city":
                        city = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(name != null);
            Assertion.eval(code != null);
            Assertion.eval(city != null);

            return new Airport(name, code, city);
        }
    }
}
