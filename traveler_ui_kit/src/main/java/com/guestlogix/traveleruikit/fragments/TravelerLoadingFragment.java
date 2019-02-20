package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guestlogix.traveleruikit.R;

/**
 * A fragment to show loading state.
 */
public class TravelerLoadingFragment extends Fragment {

    public TravelerLoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_traveler_loading, container, false);
    }
}
