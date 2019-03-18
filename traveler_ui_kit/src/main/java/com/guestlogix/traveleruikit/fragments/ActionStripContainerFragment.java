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
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.PurchaseStrategy;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;

/**
 * Fragment to contain Action strip based on Purchased Strategy
 */
public class ActionStripContainerFragment extends BaseFragment {

    public ActionStripContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_action_strip_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CatalogItemDetailsViewModel vm = ViewModelProviders.of(getActivityContext()).get(CatalogItemDetailsViewModel.class);

        vm.getObservableCatalogItemDetails().observe(this, catalogItemDetails -> {
            FragmentTransaction fragmentTransaction = getActivityContext().getSupportFragmentManager().beginTransaction();
            Fragment fragment;

            PurchaseStrategy strategy = catalogItemDetails.getPurchaseStrategy();

            switch (strategy) {
                case Bookable:
                    fragment = new BookableActionStripFragment();
                    break;

                case Buyable:
                    fragment = new BuyableActionStripFragment();
                    break;

                default:
                    fragment = new BookableActionStripFragment();
                    break;
            }
            fragmentTransaction.replace(R.id.actionStripContainerFrameLayout, fragment);
            fragmentTransaction.commit();
        });
    }
}
