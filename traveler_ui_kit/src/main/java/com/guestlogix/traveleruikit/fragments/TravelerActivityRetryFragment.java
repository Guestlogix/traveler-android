package com.guestlogix.traveleruikit.fragments;

import android.content.Context;
import android.os.Bundle;
import com.guestlogix.travelercorekit.TravelerLog;

/**
 * Generic error fragment to be used by Activities.
 * Activity must implement {@link RetryFragmentInteractionListener} to receive user actions.
 */
public class TravelerActivityRetryFragment extends TravelerRetryFragment {

    @Override
    void onAttachFragment(Context context) {
        if (context instanceof RetryFragmentInteractionListener) {
            onErrorFragmentInteractionListener = (RetryFragmentInteractionListener) context;
        } else {
            TravelerLog.e(context.toString()
                    + " must implement RetryFragmentInteractionListener");
        }
    }

    public static TravelerActivityRetryFragment getInstance(String title, String message, String action) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_ERROR_TITLE, title);
        arguments.putString(ARG_ERROR_MESSAGE, message);
        arguments.putString(ARG_ERROR_ACTION, action);

        TravelerActivityRetryFragment errorFragment = new TravelerActivityRetryFragment();
        errorFragment.setArguments(arguments);
        return errorFragment;
    }
}
