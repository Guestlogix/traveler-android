package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.security.spec.ECField;

public class CustomerContact implements Serializable {
    private String title;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private CustomerContact(String title, @NonNull String firstName, @NonNull String lastName, String email, String phone) {
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
        @Override
        public CustomerContact instantiate(JsonReader reader) throws Exception {
            String title = null;
            String fName = null;
            String lName = null;
            String email = null;
            String phone = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "firstName":
                        fName = reader.nextString();
                        break;
                    case "lastName":
                        lName = reader.nextString();
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

            Assertion.eval(fName != null);
            Assertion.eval(lName != null);

            return new CustomerContact(title, fName, lName, email, phone);
        }
    }
}
