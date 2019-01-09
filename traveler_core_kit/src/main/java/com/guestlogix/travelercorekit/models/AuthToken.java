package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.Log;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.network.MappingException;
import com.guestlogix.travelercorekit.network.MappingFactory;

import java.io.IOException;

public class AuthToken {

    String mValue;

    public AuthToken(String mValue) {
        this.mValue = mValue;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String token) {
        this.mValue = token;
    }

    //static inner class for MappingFactory<T> implementation
    public static class AuthTokenMappingFactory implements MappingFactory<AuthToken> {
        @Override
        public AuthToken instantiate(JsonReader reader) throws MappingException {
            try {
                return readAuthToken(reader);
            } catch (IOException e) {
                Log.e("AuthToken", "Error while reading token");
                throw new MappingException(new TravelerError(TravelerErrorCode.PARSING_ERROR, "IOException has occurred"));
            }
        }

        private AuthToken readAuthToken(JsonReader reader) throws IOException {
            String value = "";

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();

                if (name.equals("token")) {
                    value = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return new AuthToken(value);
        }
    }
}
