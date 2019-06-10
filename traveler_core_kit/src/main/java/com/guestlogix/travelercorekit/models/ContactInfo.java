package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactInfo implements Serializable {
    private String name;
    private String email;
    private String website;
    private String address;
    private List<String> phones;

    ContactInfo() {
    }

    private ContactInfo(String name, String email, String website, String address, List<String> phones) throws IllegalArgumentException {
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

    /**
     * Factory class to construct ContactInfo model from {@code JsonReader}.
     */
    static class ContactInfoObjectMappingFactory implements ObjectMappingFactory<ContactInfo> {
        /**
         * Parses a reader object into ContactInfo model.
         *
         * @param reader object to parse from.
         * @return ContactInfo model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or missing required field.
         */
        @Override
        public ContactInfo instantiate(JsonReader reader) throws Exception {
            String key;
            String name = "";
            String email = "";
            String website = "";
            String address = "";
            List<String> phones = new ArrayList<>();

            reader.beginObject();

            while (reader.hasNext()) {
                key = reader.nextName();

                switch (key) {
                    case "name":
                        name = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "email":
                        email = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "website":
                        website = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "address":
                        address = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "phones":
                        if (reader.peek() != JsonToken.NULL) {
                            phones.addAll(JsonReaderHelper.nextStringsArray(reader));
                        } else {
                            reader.skipValue();
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
