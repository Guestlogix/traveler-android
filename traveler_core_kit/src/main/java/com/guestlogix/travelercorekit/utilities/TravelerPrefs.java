package com.guestlogix.travelercorekit.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class TravelerPrefs {

    private static final String TRAVELER_PREFS = "TRAVELER_PREFS";

    public enum Key {
        TRAVELER_SDK_ENDPOINT,
        TRAVELER_AUTH_ENDPOINT
    }

    private static volatile TravelerPrefs travelerPrefs;
    private static SharedPreferences sharedPreferences;

    private TravelerPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(TRAVELER_PREFS, Context.MODE_PRIVATE);
    }

    public static TravelerPrefs getInstance(Context context) {
        if (travelerPrefs == null) {
            travelerPrefs = new TravelerPrefs(context);
        }

        return travelerPrefs;
    }

    public void save(Key key, String value) {
        sharedPreferences.edit().putString(key.name(), value).apply();
    }

    public void remove(Key key) {
        sharedPreferences.edit().remove(key.name()).apply();
    }

    public String get(Key key, String defaultValue) {
        return sharedPreferences.contains(key.name()) ? sharedPreferences.getString(key.name(), defaultValue) : defaultValue;
    }
}
