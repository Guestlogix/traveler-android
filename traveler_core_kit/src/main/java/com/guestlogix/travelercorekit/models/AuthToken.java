package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.Log;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;

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

    //static inner class for ObjectMappingFactory<T> implementation
    public static class AuthTokenObjectMappingFactory implements ObjectMappingFactory<AuthToken> {
        @Override
        public AuthToken instantiate(JsonReader reader) throws ObjectMappingException {
            try {
                return readAuthToken(reader);
            } catch (IOException e) {
                Log.e("AuthToken", "Error while reading token");
                throw new ObjectMappingException(new TravelerError(TravelerErrorCode.PARSING_ERROR, "IOException has occurred"));
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
