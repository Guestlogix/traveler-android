package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
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
         * @param reader Object to parse from.
         * @return ContactInfo model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public ContactInfo instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "ContactInfo";
            try {
                String name = "";
                String email = "";
                String website = "";
                String address = "";
                List<String> phones = new ArrayList<>();

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

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
                            if (reader.peek() != JsonToken.NULL) {
                                phones.addAll(JsonReaderHelper.readStringsArray(reader));
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
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
