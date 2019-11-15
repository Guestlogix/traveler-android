package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.StringArrayMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactInfo implements Serializable {
    private String name;
    private String email;
    // TODO: Use URL for this
    private String website;
    private String address;
    private List<String> phones;

    private ContactInfo(@NonNull String name, String email, String website, String address, @NonNull List<String> phones) {
        this.name = name;
        this.email = email;
        this.website = website;
        this.address = address;
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public List<String> getPhones() {
        return phones;
    }

    static class ContactInfoObjectMappingFactory implements ObjectMappingFactory<ContactInfo> {
        @Override
        public ContactInfo instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String name = jsonObject.getString("name");
            String email = jsonObject.getNullableString("email");
            String website = jsonObject.getNullableString("website");
            String address = jsonObject.getNullableString("address");
            List<String> phones = new ArrayList<>();
            if (!jsonObject.isNull("phones"))
                phones = new StringArrayMappingFactory().instantiate(jsonObject.getJSONArray("phones").toString());

            Assertion.eval(name != null);
            Assertion.eval(phones != null);

            return new ContactInfo(name, email, website, address, phones);
        }
    }
}
