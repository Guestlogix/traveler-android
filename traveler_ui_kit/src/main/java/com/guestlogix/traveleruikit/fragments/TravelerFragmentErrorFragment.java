package com.guestlogix.traveleruikit.fragments;

import android.content.Context;
import androidx.fragment.app.Fragment;

/**
 * Generic error fragment to be used by Fragments.
 * Fragment must implement {@link OnErrorInteractionListener} to receive user actions.
 */
public class TravelerFragmentErrorFragment extends TravelerErrorFragment {

    @Override
    void onAttachFragment(Context context) {
        Fragment navFragment = getParentFragment();

        if (null != navFragment) {
            Fragment fragment = navFragment.getParentFragment();

            if (fragment instanceof OnErrorInteractionListener) {
                onErrorFragmentInteractionListener = (OnErrorInteractionListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement OnErrorInteractionListener");
            }
        }
    }
}
