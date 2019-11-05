package com.guestlogix.travelercorekit.models;

/**
 * Session holds the api key, context, token and user Id.
 */
public class Session {
    private String apiKey;
    private Token token = null;
    private String identity = null;

    Session(String apiKey) {
        this.apiKey = apiKey;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token mToken) {
        this.token = mToken;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }
}
