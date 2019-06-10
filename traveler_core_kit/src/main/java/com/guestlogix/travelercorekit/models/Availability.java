package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.Date;

/**
 * Availability for a product on a specific date.
 * <p>
 * A non-null {@link BookingOptionSet} indicates that flavors exist for this availability and that the user must select
 * an {@link BookingOption} to book a product.
 */
public class Availability implements Serializable {
    private String id;
    private Date date;
    private BookingOptionSet bookingOptionSet;

    private Availability(String id, Date date, BookingOptionSet bookingOptionSet) {

        if (date == null) {
            throw new IllegalArgumentException("date can not be null");
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

    @Nullable
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
         * @param reader object to parse from.
         * @return Availability model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */
        @Override
        public Availability instantiate(JsonReader reader) throws Exception {
            Date date = null;
            String id = null;
            BookingOptionSet bookingOptionSet = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "date":
                        date = DateHelper.parseDate(JsonReaderHelper.nextNullableString(reader));
                        break;
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "optionSet":
                        if (JsonToken.NULL != reader.peek()) {
                            bookingOptionSet = new BookingOptionSet.BookingOptionSetObjectMappingFactory().instantiate(reader);
                        } else {
                            bookingOptionSet = null;
                            reader.skipValue();
                        }
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != date, "date can not be null");

            return new Availability(id, date, bookingOptionSet);
        }
    }
}
