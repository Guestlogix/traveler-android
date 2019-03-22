package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

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
            String key="Location";
            try {
                String address = "";
                Double latitude = null;
                Double longitude = null;

                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "address":
                            address = JsonReaderHelper.readString(reader);
                            break;
                        case "latitude":
                            latitude = JsonReaderHelper.readNonNullDouble(reader);
                            break;
                        case "longitude":
                            longitude = JsonReaderHelper.readNonNullDouble(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new Location(address, latitude, longitude);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }

}

