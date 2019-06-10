package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class CustomerContact implements Serializable {
    private String title;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private CustomerContact(String title, String firstName, String lastName, @NonNull String email, String phone) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email cannot be empty");
        } else {
            this.email = email;
        }

        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    static class CustomerContactObjectMappingFactory implements ObjectMappingFactory<CustomerContact> {
        /**
         * Parses a reader object into CustomerContact model.
         *
         * @param reader object to parse from.
         * @return CustomerContact model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or missing required field.
         */
        @Override
        public CustomerContact instantiate(JsonReader reader) throws Exception {
            String title = null;
            String fName = null;
            String lName = null;
            String email = null;
            String phone = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "firstName":
                        fName = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "lastName":
                        lName = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "email":
                        email = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "phone":
                        phone = JsonReaderHelper.nextNullableString(reader);
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(null != email && !email.isEmpty(), "email can not be empty");

            return new CustomerContact(title, fName, lName, email, phone);
        }
    }
}
