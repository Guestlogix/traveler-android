package com.guestlogix.travelercorekit.models;

import android.content.Context;

public class Session {
    private String apiKey;
    private Context context;
    private Token token;

    public Session(String apiKey, Context context) {
        this(apiKey, context, new Token(""));
    }

    public Session(String apiKey, Context context, Token token) {
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
