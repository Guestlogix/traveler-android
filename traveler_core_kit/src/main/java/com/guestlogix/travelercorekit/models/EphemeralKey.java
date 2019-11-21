package com.guestlogix.travelercorekit.models;

import android.util.Base64;
import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class EphemeralKey implements Serializable {
    private String key;

    EphemeralKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    static class EphemeralKeyObjectMappingFactory implements ObjectMappingFactory<EphemeralKey> {
        @Override
        public EphemeralKey instantiate(JsonReader reader) throws Exception {
            String rawKey = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "key":
                        String base64Encoded = reader.nextString();
                        rawKey = new String(Base64.decode(base64Encoded, Base64.DEFAULT));
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(rawKey != null);

            return new EphemeralKey(rawKey);
        }
    }
}
