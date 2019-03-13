package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;

public class Token {
    private String value;

    Token(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value can not be null");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String token) {
        this.value = token;
    }

    /**
     * Factory class to construct Token model from {@code JsonReader}.
     */
    public static class AuthTokenObjectMappingFactory implements ObjectMappingFactory<Token> {

        /**
         * Parses a reader object into Token.
         *
         * @param reader Object to parse from.
         * @return Token model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */

        @Override
        public Token instantiate(JsonReader reader) throws ObjectMappingException {
            try {
                String value = "";

                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();

                    if (name.equals("token")) {
                        value = JsonReaderHelper.readNonNullString(reader);
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
                return new Token(value);
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, e.getMessage()));

            }
        }
    }
}
