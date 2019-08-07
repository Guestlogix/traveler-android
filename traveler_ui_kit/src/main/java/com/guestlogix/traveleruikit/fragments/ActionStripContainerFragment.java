package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.PurchaseStrategy;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.models.BookingContext;
import com.guestlogix.traveleruikit.models.PurchaseContext;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;

/**
 * Fragment to contain Action strip based on Purchased Strategy
 */
public class ActionStripContainerFragment extends Fragment {
    public static final String ARG_ITEM_DETAILS = "ARG_ITEM_DETAILS";

    private PurchaseContext purchaseContext;
    private PurchaseFragment fragment;

    public static ActionStripContainerFragment newInstance(CatalogItemDetails itemDetails) {
        ActionStripContainerFragment fragment = new ActionStripContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_DETAILS, itemDetails);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action_strip_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
