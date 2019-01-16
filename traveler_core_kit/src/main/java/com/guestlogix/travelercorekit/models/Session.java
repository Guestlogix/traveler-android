package com.guestlogix.travelercorekit.models;

import android.content.Context;

public class Session {
    private String mApiKey;
    private Context mContext;
    private AuthToken mAuthToken;

    public Session(String mApiKey, Context mContext) {
        this(mApiKey, mContext, new AuthToken(""));
    }

    public Session(String mApiKey, Context mContext, AuthToken mAuthToken) {
        this.mAuthToken = mAuthToken;
        this.mContext = mContext;
        this.mApiKey = mApiKey;
    }

    public AuthToken getAuthToken() {
        return mAuthToken;
    }

    public void setAuthToken(AuthToken mAuthToken) {
        this.mAuthToken = mAuthToken;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public Context getContext() {
        return mContext;
    }

}
