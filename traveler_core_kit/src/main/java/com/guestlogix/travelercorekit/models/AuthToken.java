package com.guestlogix.travelercorekit.models;

import com.guestlogix.network.MappingException;
import com.guestlogix.network.MappingFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthToken {

    String token;

    public AuthToken(String token) {
        this.token = token;
    }

    public AuthToken(JSONObject jsonObject) throws MappingException {

        try {
            this.token = jsonObject.getString("token");
        } catch (JSONException e) {
            throw new MappingException();
        }

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //static inner class for MappingFactory<T> implementation
    public static class AuthTokenMappingFactory implements MappingFactory<AuthToken> {
        @Override
        public AuthToken instantiate(JSONObject jsonObject) throws MappingException {
            return new AuthToken(jsonObject);
        }
    }
}
