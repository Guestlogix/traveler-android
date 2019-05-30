package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;

public class Location implements Serializable {
    private String address;
    private double latitude;
    private double longitude;

    private Location(String address, double latitude, double longitude) throws ObjectMappingException {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
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
            String key = "Location";
            try {
                String address = "";
                double latitude = 0;
                double longitude = 0;

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
            } catch (ObjectMappingException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }

}

