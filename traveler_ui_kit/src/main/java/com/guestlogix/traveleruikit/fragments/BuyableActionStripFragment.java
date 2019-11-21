package com.guestlogix.traveleruikit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.BookableProductViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

/**
 * TODO: This fragment is not yet implemented! It is an exact copy of Bookable Fragment.
 */
public class BuyableActionStripFragment extends BaseFragment {

    private ActionStrip actionStrip;
    private BookableProductViewModel sharedViewModel;

    public BuyableActionStripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookable_action_strip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO
    }
}
