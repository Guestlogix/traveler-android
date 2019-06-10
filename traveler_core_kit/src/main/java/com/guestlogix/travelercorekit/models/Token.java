package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
         * @param reader object to parse from.
         * @return Token model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or missing required field.
         */
        @Override
        public Token instantiate(JsonReader reader) throws Exception {
            String value = "";

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                if (key.equals("token")) {
                    value = JsonReaderHelper.nextNullableString(reader);
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != value, "value can not be null");

            return new Token(value);
        }
    }
}
