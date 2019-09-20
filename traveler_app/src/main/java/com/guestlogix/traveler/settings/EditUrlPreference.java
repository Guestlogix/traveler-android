package com.guestlogix.traveler.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.URLUtil;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

public class EditUrlPreference extends EditTextPreference {

    public EditUrlPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnPreferenceChangeListener(new OnUrlPreferenceChangeListener());
    }

    class OnUrlPreferenceChangeListener implements OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (URLUtil.isValidUrl(String.valueOf(newValue))) {
                preference.setSummary(String.valueOf(newValue));
                return true;
            }
            return false;
        }
    }
}
