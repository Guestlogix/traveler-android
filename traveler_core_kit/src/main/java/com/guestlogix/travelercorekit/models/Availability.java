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

public class Availability {
    Date date;
    ArrayList<Long> times;
    Boolean isAvailable;

    public Availability(Date date, ArrayList<Long> times, Boolean isAvailable) {
        this.date = date;
        this.times = times;
        this.isAvailable = isAvailable;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Long> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Long> times) {
        this.times = times;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public static class AvailabilityObjectMappingFactory implements ObjectMappingFactory<Availability> {

        @Override
        public Availability instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            return readItem(reader);

        }

        private Availability readItem(JsonReader reader) throws IOException {
            Date date = new Date();
            ArrayList<Long> timeList = new ArrayList<>();
            Boolean available = false;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "date":
                        try {
                            date = DateHelper.getDateAsObject(JsonReaderHelper.readString(reader));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "timesInMinutes":
                        if (reader.peek() != JsonToken.NULL) {
                            timeList = JsonReaderHelper.readLongArray(reader);
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
