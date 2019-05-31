package com.guestlogix.traveleruikit.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.guestlogix.travelercorekit.TravelerLog;

/**
 * Generic error fragment to be used by Fragments.
 * Fragment must implement {@link RetryFragmentInteractionListener} to receive user actions.
 */
public class TravelerFragmentRetryFragment extends TravelerRetryFragment {

    @Override
    void onAttachFragment(Context context) {
        Fragment navFragment = getParentFragment();

        if (null != navFragment) {
            Fragment fragment = navFragment.getParentFragment();

            if (fragment instanceof RetryFragmentInteractionListener) {
                onErrorFragmentInteractionListener = (RetryFragmentInteractionListener) fragment;
            } else {
                TravelerLog.e(fragment.toString() + " must implement RetryFragmentInteractionListener");
            }
        }
    }

    public static TravelerFragmentRetryFragment getInstance(String title, String message, String action) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_ERROR_TITLE, title);
        arguments.putString(ARG_ERROR_MESSAGE, message);
        arguments.putString(ARG_ERROR_ACTION, action);

        TravelerFragmentRetryFragment errorFragment = new TravelerFragmentRetryFragment();
        errorFragment.setArguments(arguments);
        return errorFragment;
    }
}
