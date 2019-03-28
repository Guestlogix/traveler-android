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
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.BookableProductViewModel;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.traveleruikit.viewmodels.ProductViewModel;

public class InformationSelectionContainerFragment extends BaseFragment {
    private CatalogItemDetailsViewModel viewModel;

    public InformationSelectionContainerFragment() {
        // Do nothing.
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information_selection_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivityContext()).get(CatalogItemDetailsViewModel.class);
        viewModel.getObservableCatalogItemDetails().observe(this, this::onCatalogItemDetailsChanged);
    }

    private void onCatalogItemDetailsChanged(CatalogItemDetails catalogItemDetails) {
        PurchaseStrategy strategy = catalogItemDetails.getPurchaseStrategy();

        FragmentTransaction fragmentTransaction = getActivityContext().getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        ProductViewModel productVM;

        switch (strategy) {
            case Buyable:
                fragment = new BuyableInformationSelectionFragment();
                // TODO: add custom repo
                break;

            case Bookable:
            default:
                fragment = new BookableInformationSelectionFragment();
                productVM = ViewModelProviders.of(getActivityContext()).get(BookableProductViewModel.class);
                productVM.setup(catalogItemDetails);
                break;
        }

        fragmentTransaction.replace(R.id.information_selection_container, fragment);
        fragmentTransaction.commit();
    }
}
