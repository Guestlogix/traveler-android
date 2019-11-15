package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class BookingOption implements Serializable {
    private final String id;
    private String value;
    private String disclaimer;

    private BookingOption(@NonNull String id, @NonNull String value, String disclaimer) {
        this.id = id;
        this.value = value;
        this.disclaimer = disclaimer;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    static class BookingOptionObjectMappingFactory implements ObjectMappingFactory<BookingOption> {

        @Override
        public BookingOption instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String value = jsonObject.getString("optionLabel");
            String disclaimer = jsonObject.getNullableString("disclaimer");

            Assertion.eval(id != null);
            Assertion.eval(value != null);

            return new BookingOption(id, value, disclaimer);
        }
    }
}
