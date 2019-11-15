package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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

    private Availability(@NonNull String id, @NonNull Date date, BookingOptionSet bookingOptionSet) {
        this.id = id;
        this.date = date;
        this.bookingOptionSet = bookingOptionSet;
    }

    public Date getDate() {
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
        public Availability instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            Date date = DateHelper.parseDate(jsonObject.getString("date"));
            String id = jsonObject.getString("id");
            BookingOptionSet bookingOptionSet = new BookingOptionSet.BookingOptionSetObjectMappingFactory().instantiate(jsonObject.getJSONObject("optionSet").toString());


            Assertion.eval(date != null);
            Assertion.eval(id != null);

            return new Availability(id, date, bookingOptionSet);
        }
    }
}
