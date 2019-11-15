package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
import java.util.List;


public class BookingOptionSet implements Serializable {
    private String label;
    private List<BookingOption> options;

    private BookingOptionSet(@NonNull String label, @NonNull List<BookingOption> options) {
        this.label = label;
        this.options = options;
    }

    public String getLabel() {
        return label;
    }

    public List<BookingOption> getOptions() {
        return options;
    }

    static class BookingOptionSetObjectMappingFactory implements ObjectMappingFactory<BookingOptionSet> {

        @Override
        public BookingOptionSet instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String label = jsonObject.getString("optionSetLabel");
            List<BookingOption> options = new ArrayMappingFactory<>(new BookingOption.BookingOptionObjectMappingFactory()).instantiate(jsonObject.getJSONArray("options").toString());

            Assertion.eval(label != null);
            Assertion.eval(options != null);

            return new BookingOptionSet(label, options);
        }
    }
}
