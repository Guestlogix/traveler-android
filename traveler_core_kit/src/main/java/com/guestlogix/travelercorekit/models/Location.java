package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;

public class Location implements Serializable {
    private String address;
    private Double latitude;
    private Double longitude;

    private Location(String address, Double latitude, Double longitude) throws ObjectMappingException {
        this.address = address;
        if (null == latitude) {
            throw new IllegalArgumentException("latitude can not be empty");
        } else {
            this.latitude = latitude;
        }
        if (longitude == null) {
            throw new IllegalArgumentException("longitude can not be empty");
        } else {
            this.longitude = longitude;
        }
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    static class LocationObjectMappingFactory implements ObjectMappingFactory<Location> {

        @Override
        public Location instantiate(JsonReader reader) throws ObjectMappingException {
            String model = "Location";
            String key = "Location";
            try {
                String address = "";
                Double latitude = null;
                Double longitude = null;

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "address":
                            address = JsonReaderHelper.readString(reader);
                            break;
                        case "latitude":
                            latitude = JsonReaderHelper.readDouble(reader);
                            break;
                        case "longitude":
                            longitude = JsonReaderHelper.readDouble(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new Location(address, latitude, longitude);
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

