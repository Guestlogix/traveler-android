package com.guestlogix.travelercorekit.models;

import com.guestlogix.network.MappingException;
import com.guestlogix.network.MappingFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthToken {

    String token;
    //make it date
    //remove
    String created;
    String expires;

    public AuthToken(String token, String created, String expires) {
        this.token = token;
        this.created = created;
        this.expires = expires;

    }

    public AuthToken(JSONObject jsonObject) throws MappingException {

        try {
            this.token = jsonObject.getString("token");
            this.created = jsonObject.getString("created");
            this.expires = jsonObject.getString("expires");

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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    //static inner class for MappingFactory<T> implementation
    public static class AuthTokenMappingFactory implements MappingFactory<AuthToken> {

        @Override
        public AuthToken instantiate(JSONObject jsonObject) throws MappingException {
            return new AuthToken(jsonObject);
        }
    }
}
