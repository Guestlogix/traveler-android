package com.guestlogix.traveler.fragments;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.guestlogix.traveler.R;
import com.guestlogix.traveler.settings.TravelerDataStore;
import com.guestlogix.travelercorekit.utilities.TravelerPrefs;

import static com.guestlogix.travelercorekit.utilities.TravelerPrefs.Key.TRAVELER_AUTH_ENDPOINT;
import static com.guestlogix.travelercorekit.utilities.TravelerPrefs.Key.TRAVELER_SDK_ENDPOINT;

public class SettingsFragment extends PreferenceFragmentCompat {

    private TravelerPrefs travelerPrefs;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs, rootKey);

        travelerPrefs = TravelerPrefs.getInstance(getContext());

        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setPreferenceDataStore(new TravelerDataStore(getContext()));

        Preference sdkEndpointPreference = findPreference(TRAVELER_SDK_ENDPOINT.name());
        sdkEndpointPreference.setSummary(travelerPrefs.get(TRAVELER_SDK_ENDPOINT, ""));

        Preference resetSDKPreference = findPreference("reset_sdk_endpoint");
        resetSDKPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sdkEndpointPreference.setSummary("");
                travelerPrefs.remove(TRAVELER_SDK_ENDPOINT);
                return true;
            }
        });

        Preference authEndpointPreference = findPreference(TRAVELER_AUTH_ENDPOINT.name());
        authEndpointPreference.setSummary(travelerPrefs.get(TRAVELER_AUTH_ENDPOINT, ""));

        Preference resetAuthPreference = findPreference("reset_auth_endpoint");
        resetAuthPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                authEndpointPreference.setSummary("");
                travelerPrefs.remove(TRAVELER_AUTH_ENDPOINT);
                return true;
            }
        });
    }
}
