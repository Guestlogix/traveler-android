package com.guestlogix.traveler.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUtils {

    private static final String USER_PREF_ = "pref_user";
    public static final String TRAVELER_ID = "pref_traveler_id";
    public static final String EXTERNAL_ID = "pref_external_id";
    public static final String FIRST_NAME = "pref_first_name";
    public static final String LAST_NAME = "pref_last_name";
    public static final String EMAIL = "pref_email";

    private static SharedPrefsUtils localInstance;
    private SharedPreferences sharedPreferences;

    private SharedPrefsUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_PREF_, Context.MODE_PRIVATE);
    }

    public static SharedPrefsUtils getInstance(Context activity) {
        if (null == localInstance) {
            localInstance = new SharedPrefsUtils(activity);
        }
        return localInstance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void removeString(String key) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.remove(key);
        prefsEditor.apply();
    }
}
