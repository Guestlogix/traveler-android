package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
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

    private Availability(@NonNull String id, @NonNull Date date, BookingOptionSet bookingOptionSet) {
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

    static class AvailabilityObjectMappingFactory implements ObjectMappingFactory<Availability> {
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
                        date = DateHelper.parseDate(reader.nextString());
                        break;
                    case "id":
                        id = reader.nextString();
                        break;
                    case "optionSet":
                        bookingOptionSet = new BookingOptionSet.BookingOptionSetObjectMappingFactory().instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(date != null);
            Assertion.eval(id != null);

            return new Availability(id, date, bookingOptionSet);
        }
    }
}
