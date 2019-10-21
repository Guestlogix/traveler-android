package com.guestlogix.traveleruikit.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.guestlogix.travelercorekit.callbacks.BookingItemDetailsCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.repositories.CatalogItemDetailsRepository;

public class CatalogItemDetailsViewModel extends StatefulViewModel {

    // Live Data
    private MutableLiveData<CatalogItemDetails> catalogItemDetails;
    private CatalogItemDetailsRepository catalogItemDetailsRepository;

    private Product product;

    public CatalogItemDetailsViewModel(@NonNull Application application) {
        super(application);
        this.catalogItemDetailsRepository = new CatalogItemDetailsRepository();

        catalogItemDetails = new MutableLiveData<>();
    }

    public LiveData<CatalogItemDetails> getObservableCatalogItemDetails() {
        return catalogItemDetails;
    }

    public Product getProduct() {
        return product;
    }

//    public void setCatalogItem(CatalogItem catalogItem) {
//        this.product = catalogItem;
//        updateCatalog(catalogItem);
//    }

    private BookingItemDetailsCallback productDetailsCallback = new BookingItemDetailsCallback() {
        @Override
        public void onBookingItemDetailsSuccess(CatalogItemDetails catalog) {
            status.setValue(State.SUCCESS);
            catalogItemDetails.setValue(catalog);
        }

        @Override
        public void onBookingItemDetailsError(Error error) {
            status.setValue(State.ERROR);
        }
    };

    private void updateCatalog(Product product) {
        status.setValue(State.LOADING);
        //TODO: Signatures can be changed to take Product
        catalogItemDetailsRepository.fetchDetails(product, productDetailsCallback);
    }
}