package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Availability {
    private Date date;
    private List<Long> times;
    private Boolean isAvailable;

    private Availability(Date date, List<Long> times, Boolean isAvailable) {
        this.date = date;
        this.times = times;
        this.isAvailable = isAvailable;
    }

    public Date getDate() {
        return date;
    }

    public List<Long> getTimes() {
        return times;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public static class AvailabilityObjectMappingFactory implements ObjectMappingFactory<Availability> {

        @Override
        public Availability instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            return readItem(reader);

        }

        private Availability readItem(JsonReader reader) throws ObjectMappingException, IOException {
            Date date = new Date();
            List<Long> timeList = new ArrayList<>();
            Boolean available = false;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "date":
                        try {
                            date = DateHelper.parseDate(JsonReaderHelper.readString(reader));
                        } catch (ParseException e) {
                            throw new ObjectMappingException(new ObjectMappingError(TravelerErrorCode.INVALID_DATA, "IOException has occurred"));
                        }
                        break;
                    case "timesInMinutes":
                        if (reader.peek() != JsonToken.NULL) {
                            timeList.addAll(JsonReaderHelper.readLongArray(reader));
                        }
                        break;
                    case "available":
                        available = JsonReaderHelper.readBoolean(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new Availability(date, timeList, available);
        }
    }
}
