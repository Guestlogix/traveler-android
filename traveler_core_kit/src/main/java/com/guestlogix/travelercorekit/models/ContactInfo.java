package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
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
        public ContactInfo instantiate(JsonReader reader) throws Exception {
            String name = null;
            String email = null;
            String website = null;
            String address = null;
            List<String> phones = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

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
                            phones = JsonReaderHelper.readStringsArray(reader);
                        } else {
                            phones = new ArrayList<>();
                            reader.skipValue();
                        }
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(name != null);
            Assertion.eval(phones != null);

            return new ContactInfo(name, email, website, address, phones);
        }
    }
}
