package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;

public class Token {

    private String value;

    public Token(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String token) {
        this.value = token;
    }

    //static inner class for ObjectMappingFactory<T> implementation
    public static class AuthTokenObjectMappingFactory implements ObjectMappingFactory<Token> {
        @Override
        public Token instantiate(JsonReader reader) throws ObjectMappingException {
            try {
                return readAuthToken(reader);
            } catch (IOException e) {
                throw new ObjectMappingException(new TravelerError(TravelerErrorCode.PARSING_ERROR, "IOException has occurred"));
            }
        }

        private Token readAuthToken(JsonReader reader) throws IOException {
            String value = "";

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();

                if (name.equals("token")) {
                    value = JsonReaderHelper.readString(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return new Token(value);
        }
    }
}
