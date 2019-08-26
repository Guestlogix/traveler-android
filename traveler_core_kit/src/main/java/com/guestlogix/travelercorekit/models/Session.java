package com.guestlogix.travelercorekit.models;

import android.content.Context;
import androidx.annotation.Nullable;

/**
 * Session holds the api key, context, token and user Id.
 */
public class Session {
    private String apiKey;
    private Context context;
    private Token token = null;
    private String identity = null;

    Session(String apiKey, Context context) {
        this.context = context;
        this.apiKey = apiKey;
    }

    @Nullable
    public Token getToken() {
        return token;
    }

    public void setToken(Token mToken) {
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
