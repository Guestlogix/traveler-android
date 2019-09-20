package com.guestlogix.traveler.settings;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceDataStore;

import com.guestlogix.travelercorekit.utilities.TravelerPrefs;

public class TravelerDataStore extends PreferenceDataStore {

    private TravelerPrefs travelerPrefs;

    public TravelerDataStore(Context context) {
        travelerPrefs = TravelerPrefs.getInstance(context);
    }

    @Override
    public void putString(String key, @Nullable String value) {
        travelerPrefs.save(TravelerPrefs.Key.valueOf(key), value);
    }
}
