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
public class ActionStripContainerFragment extends BaseFragment {
    private PurchaseContext purchaseContext;
    private PurchaseFragment fragment;

    public ActionStripContainerFragment() {
        // Do nothing.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action_strip_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setPurchaseContext(PurchaseContext purchaseContext) {
        // If this context is null have a fragment transaction.
        if (this.purchaseContext == null) {
            FragmentTransaction fragmentTransaction = getActivityContext().getSupportFragmentManager().beginTransaction();

            // TODO: Add more fragment types here.
            if (purchaseContext instanceof BookingContext) {
                fragment = BookableActionStripFragment.getInstance((BookingContext) purchaseContext);
            }

            fragmentTransaction.replace(R.id.actionStripContainerFrameLayout, fragment);
            fragmentTransaction.commit();
        } else {
            fragment.setPurchaseContext(purchaseContext);
        }

        this.purchaseContext = purchaseContext;
    }
}
