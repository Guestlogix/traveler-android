package com.guestlogix.traveler.fragments;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.guestlogix.traveler.R;
import com.guestlogix.traveler.network.GuestRoute;
import com.guestlogix.traveler.settings.EditUrlPreference;
import com.guestlogix.traveler.settings.TravelerDataStore;
import com.guestlogix.travelercorekit.Router;
import com.guestlogix.travelercorekit.utilities.TravelerPrefs;

import static com.guestlogix.travelercorekit.utilities.TravelerPrefs.Key.TRAVELER_AUTH_ENDPOINT;
import static com.guestlogix.travelercorekit.utilities.TravelerPrefs.Key.TRAVELER_SDK_ENDPOINT;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG";
    private TravelerPrefs travelerPrefs;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs, rootKey);

        travelerPrefs = TravelerPrefs.getInstance(getContext());

        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setPreferenceDataStore(new TravelerDataStore(getContext()));

        EditUrlPreference sdkEndpointPreference = (EditUrlPreference) findPreference(TRAVELER_SDK_ENDPOINT.name());
        String currentSdkEndpoint = travelerPrefs.get(TRAVELER_SDK_ENDPOINT, Router.DEFAULT_ENDPOINT);
        sdkEndpointPreference.setSummary(currentSdkEndpoint);
        sdkEndpointPreference.setText(currentSdkEndpoint);

        Preference resetSDKPreference = findPreference("reset_sdk_endpoint");
        resetSDKPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sdkEndpointPreference.setSummary(Router.DEFAULT_ENDPOINT);
                sdkEndpointPreference.setText(Router.DEFAULT_ENDPOINT);
                travelerPrefs.remove(TRAVELER_SDK_ENDPOINT);
                Router.clearSdkEndpoint();
                return true;
            }
        });

        EditUrlPreference authEndpointPreference = (EditUrlPreference) findPreference(TRAVELER_AUTH_ENDPOINT.name());
        String currentAuthEndpoint = travelerPrefs.get(TRAVELER_AUTH_ENDPOINT, GuestRoute.DEFAULT_AUTH_URL);
        authEndpointPreference.setSummary(currentAuthEndpoint);
        authEndpointPreference.setText(currentAuthEndpoint);

        Preference resetAuthPreference = findPreference("reset_auth_endpoint");
        resetAuthPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                authEndpointPreference.setSummary(GuestRoute.DEFAULT_AUTH_URL);
                authEndpointPreference.setText(GuestRoute.DEFAULT_AUTH_URL);
                travelerPrefs.remove(TRAVELER_AUTH_ENDPOINT);
                return true;
            }
        });
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (preference instanceof EditUrlPreference) {

            EditUrlPreference editUrlPreference = (EditUrlPreference) preference;
            // check if dialog is already showing
            //TODO: change getFragmentManager(). its a deprecated api
            if (getFragmentManager() != null && getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
                return;
            }

            // A custom EditTextPreferenceDialogFragmentCompat is required here so that we can capture
            // the DialogDismissed, then we clear Router's cached RouteBuilder.travelerSDKEndpoint
            UrlEditTextPreferenceDialogFragmentCompat dialogFragment =
                    UrlEditTextPreferenceDialogFragmentCompat.newInstance(
                            preference.getKey(),
                            editUrlPreference.getSummary().toString());
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
