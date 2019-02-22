package com.guestlogix.travelercorekit.tasks;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public abstract class SharedPrefsTask extends Task {

    Context mContext;
    String mKey;
    String MY_PREFS_NAME = "TRAVELER";
    SharedPreferences mSharedPreferences;

    SharedPrefsTask(Context mContext, String key) {
        this.mContext = mContext;
        this.mKey = key;
        mSharedPreferences = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
