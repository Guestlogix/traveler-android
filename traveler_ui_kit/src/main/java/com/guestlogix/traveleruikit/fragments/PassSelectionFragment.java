package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guestlogix.traveleruikit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassSelectionFragment extends Fragment {

    public PassSelectionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass_selection, container, false);
    }

}
