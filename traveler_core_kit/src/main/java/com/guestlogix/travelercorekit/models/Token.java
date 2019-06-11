package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;

public class Token {
    private String value;

    public Token(String value) {
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
            String key = "Token";
            try {
                String value = "";

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();
                while (reader.hasNext()) {
                    key = reader.nextName();

                    if (key.equals("token")) {
                        value = JsonReaderHelper.readNonNullString(reader);
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
                return new Token(value);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
