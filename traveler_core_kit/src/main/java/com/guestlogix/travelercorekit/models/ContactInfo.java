package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactInfo implements Serializable {

    private String name;
    private String email;
    private String website;
    private String address;
    private List<String> phones;

    public ContactInfo() {
    }

    public ContactInfo(String name, String email, String website, String address, List<String> phones) {
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
            List<String> phones = new ArrayList<>();

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
                    case "phones":
                        if(reader.peek() != JsonToken.NULL) {
                            phones.addAll(JsonReaderHelper.readStringsArray(reader));
                        }
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new ContactInfo(name, email, website, address, phones);
        }
    }
}
