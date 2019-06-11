package com.guestlogix.travelercorekit.models;

import android.content.Context;

/**
 * Session holds the api key, context, token and user Id.
 */
public class Session {
    private String apiKey;
    private Context context;
    private Token token;
    private String identity;

    Session(String apiKey, Context context) {
        this(apiKey, context, new Token(""), null);
    }

    private Session(String apiKey, Context context, Token token, String identity) {
        this.token = token;
        this.context = context;
        this.apiKey = apiKey;
        this.identity = identity;
    }

    public Token getAuthToken() {
        return token;
    }

    public void setAuthToken(Token mToken) {
        this.token = mToken;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Context getContext() {
        return context;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }
}
