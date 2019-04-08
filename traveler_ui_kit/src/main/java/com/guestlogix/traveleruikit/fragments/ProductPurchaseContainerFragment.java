package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.PurchaseStrategy;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.models.BookingContext;
import com.guestlogix.traveleruikit.models.PurchaseContext;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;

public class ProductPurchaseContainerFragment extends BaseFragment implements BookingProductFragment.BookingContextChangedListener {

    private PurchaseContextChangedListener purchaseContextChangedListener;

    public ProductPurchaseContainerFragment() {
        // Do nothing.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information_selection_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CatalogItemDetailsViewModel viewModel = ViewModelProviders.of(getActivityContext()).get(CatalogItemDetailsViewModel.class);
        viewModel.getObservableCatalogItemDetails().observe(this, this::onCatalogItemDetailsChanged);
    }

    private void onCatalogItemDetailsChanged(CatalogItemDetails catalogItemDetails) {
        if (catalogItemDetails == null) {
            return;
        }

        PurchaseStrategy strategy = catalogItemDetails.getPurchaseStrategy();
        FragmentTransaction fragmentTransaction = getActivityContext().getSupportFragmentManager().beginTransaction();

        switch (strategy) {
            case Buyable:
                // TODO: add custom repo
                break;

            case Bookable:
            default:
                BookingProductFragment fragment = BookingProductFragment.getInstance(catalogItemDetails);
                fragment.setBookingContextChangedListener(this);
                fragmentTransaction.replace(R.id.information_selection_container, fragment);
                break;
        }

        fragmentTransaction.commit();
    }

    public void setPurchaseContextChangedListener(PurchaseContextChangedListener purchaseContextChangedListener) {
        this.purchaseContextChangedListener = purchaseContextChangedListener;
    }

    @Override
    public void onBookingContextChanged(BookingContext bookingContext) {
        if (purchaseContextChangedListener != null) {
            purchaseContextChangedListener.onPurchaseContextChanged(bookingContext);
        }
    }

    public interface PurchaseContextChangedListener {
        void onPurchaseContextChanged(PurchaseContext purchaseContext);
    }
}
