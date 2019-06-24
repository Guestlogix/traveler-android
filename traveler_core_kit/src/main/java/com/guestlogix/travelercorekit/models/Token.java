package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;


public class Token {
    private String value;

    public Token(@NonNull String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String token) {
        this.value = token;
    }

    public static class AuthTokenObjectMappingFactory implements ObjectMappingFactory<Token> {
        @Override
        public Token instantiate(JsonReader reader) throws Exception {
            String value = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                if (key.equals("token")) {
                    value = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(value != null);

            return new Token(value);
        }
    }
}
