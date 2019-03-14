package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class Availability implements Serializable {
    private String id;
    private Date date;
    private BookingOptionSet bookingOptionSet;

    private Availability(String id, Date date, BookingOptionSet bookingOptionSet) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }

        if (date == null) {
            throw new IllegalArgumentException("date can not be null");
        }

        if (bookingOptionSet == null) {
            throw new IllegalArgumentException("bookingOptionSet can not be null");
        }

        this.id = id;
        this.date = date;
        this.bookingOptionSet = bookingOptionSet;
    }

    Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public BookingOptionSet getBookingOptionSet() {
        return bookingOptionSet;
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
                Date date = null;
                String id = null;
                BookingOptionSet bookingOptionSet = null;

                reader.beginObject();

                while (reader.hasNext()) {
                    String name = reader.nextName();

                    switch (name) {
                        case "date":

                            date = DateHelper.parseDate(JsonReaderHelper.readString(reader));
                            break;
                        case "id":
                            id = JsonReaderHelper.readString(reader);
                            break;
                        case "optionSet":
                            bookingOptionSet = new BookingOptionSet.BookingOptionSetObjectMappingFactory().instantiate(reader);
                            break;
                        default:
                            reader.skipValue();

                    }
                }

                reader.endObject();

                return new Availability(id, date, bookingOptionSet);
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
