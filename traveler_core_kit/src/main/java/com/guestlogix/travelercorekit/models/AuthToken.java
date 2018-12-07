package com.guestlogix.travelercorekit.models;

import com.guestlogix.network.MappingException;
import com.guestlogix.network.MappingFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthToken {

    String mValue;

    public AuthToken(String mValue) {
        this.mValue = mValue;
    }

    public AuthToken(JSONObject jsonObject) throws MappingException {

        try {
            this.mValue = jsonObject.getString("token");
        } catch (JSONException e) {
            throw new MappingException();
        }
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
        public AuthToken instantiate(JSONObject jsonObject) throws MappingException {
            return new AuthToken(jsonObject);
        }
    }
}
