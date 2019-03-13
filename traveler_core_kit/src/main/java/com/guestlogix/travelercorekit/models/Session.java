package com.guestlogix.travelercorekit.models;

import android.content.Context;

/**
 * Session holds the api key, context and token.
 */
public class Session {
    private String apiKey;
    private Context context;
    private Token token;

    Session(String apiKey, Context context) {
        this(apiKey, context, new Token(""));
    }

    private Session(String apiKey, Context context, Token token) {
        this.token = token;
        this.context = context;
        this.apiKey = apiKey;
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
}
