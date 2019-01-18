package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;

public class ContactInfo {

    private String name;
    private String email;
    private String website;
    private String phones;

    public ContactInfo() {
    }

    public ContactInfo(String name, String email, String website, String phones) {
        this.name = name;
        this.email = email;
        this.website = website;
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public static class ContactInfoObjectMappingFactory implements ObjectMappingFactory<ContactInfo> {

        @Override
        public ContactInfo instantiate(JsonReader reader) throws IOException {
            return readContactInfo(reader);
        }

        private ContactInfo readContactInfo(JsonReader reader) throws IOException {
            String name = "";
            String email = "";
            String website = "";
            String address = "";

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "name":
                        name = JsonReaderHelper.readString(reader);
                        break;
                    case "email":
                        email = JsonReaderHelper.readString(reader);
                        break;
                    case "website":
                        website = JsonReaderHelper.readString(reader);
                        break;
                    case "address":
                        address = JsonReaderHelper.readString(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new ContactInfo(name, email, website, address);
        }
    }
}
