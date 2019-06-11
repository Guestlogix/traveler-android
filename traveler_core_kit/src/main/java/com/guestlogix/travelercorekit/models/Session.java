package com.guestlogix.travelercorekit.models;

import android.content.Context;

/**
 * Session holds the api key, context, token and user Id.
 */
public class Session {
    private String apiKey;
    private Context context;
    private Token token;
    private String userId;

    Session(String apiKey, Context context) {
        this.token = token;
        this.context = context;
        this.apiKey = apiKey;
        this.userId = userId;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
