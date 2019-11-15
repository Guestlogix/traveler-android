package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;

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
        public CustomerContact instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String title = jsonObject.getNullableString("title");
            String fName = jsonObject.getString("firstName");
            String lName = jsonObject.getString("lastName");
            String email = jsonObject.getNullableString("email");
            String phone = jsonObject.getNullableString("phone");

            Assertion.eval(fName != null);
            Assertion.eval(lName != null);

            return new CustomerContact(title, fName, lName, email, phone);
        }
    }
}
