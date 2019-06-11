package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;

public class CustomerContact implements Serializable {
    private String title;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private CustomerContact(String title, String firstName, String lastName, @NonNull String email, String phone) {
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
        public CustomerContact instantiate(JsonReader reader) throws ObjectMappingException, IOException {
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
                        title = JsonReaderHelper.readString(reader);
                        break;
                    case "firstName":
                        fName = JsonReaderHelper.readString(reader);
                        break;
                    case "lastName":
                        lName = JsonReaderHelper.readString(reader);
                        break;
                    case "email":
                        email = JsonReaderHelper.readNonNullString(reader);
                        break;
                    case "phone":
                        phone = JsonReaderHelper.readString(reader);
                        break;
                }
            }

            reader.endObject();

            if (null == email) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "Payload email must not be null"));
            }

            return new CustomerContact(title, fName, lName, email, phone);
        }
    }
}
