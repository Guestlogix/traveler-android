package com.guestlogix.travelercorekit.models;

import android.util.Base64;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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
        public EphemeralKey instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String rawKey = new String(Base64.decode(jsonObject.getString("key"), Base64.DEFAULT)) ;

            Assertion.eval(rawKey != null);

            return new EphemeralKey(rawKey);
        }
    }
}
