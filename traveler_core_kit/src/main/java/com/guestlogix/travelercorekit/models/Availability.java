package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Availability {
    private Date date;
    private List<Long> times;
    private Boolean isAvailable;

    private Availability(Date date, List<Long> times, Boolean isAvailable) {
        if (null == date) {
            throw new IllegalArgumentException("date can not be null");
        } else {
            this.date = date;
        }
        if (null == times) {
            throw new IllegalArgumentException("times can not be null");
        } else {
            this.times = times;
        }
        if (null == isAvailable) {
            throw new IllegalArgumentException("isAvailable can not be empty");
        } else {
            this.isAvailable = isAvailable;
        }
    }

    Date getDate() {
        return date;
    }

    List<Long> getTimes() {
        return times;
    }

    boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Factory class to construct Availability model from {@code JsonReader}.
     */
    static class AvailabilityObjectMappingFactory implements ObjectMappingFactory<Availability> {
        /**
         * Parses a reader object into Availability model.
         *
         * @param reader Object to parse from.
         * @return Availability model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public Availability instantiate(JsonReader reader) throws ObjectMappingException {
            try {
                Date date = new Date();
                List<Long> timeList = new ArrayList<>();
                Boolean available = false;

                reader.beginObject();

                while (reader.hasNext()) {
                    String name = reader.nextName();

                    switch (name) {
                        case "date":
                            date = DateHelper.parseDate(JsonReaderHelper.readString(reader));
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
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, e.getMessage()));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            } catch (ParseException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, e.getMessage()));
            }
        }
    }
}
