package com.guestlogix.traveleruikit.fragments;

import android.content.Context;

/**
 * Generic error fragment to be used by Activities.
 * Activity must implement {@link OnErrorInteractionListener} to receive user actions.
 */
public class TravelerActivityErrorFragment extends TravelerErrorFragment {

    @Override
    void onAttachFragment(Context context) {
        if (context instanceof OnErrorInteractionListener) {
            onErrorFragmentInteractionListener = (OnErrorInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnErrorInteractionListener");
        }
    }
}
