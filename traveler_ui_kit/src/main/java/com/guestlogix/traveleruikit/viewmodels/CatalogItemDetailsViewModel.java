package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.repositories.CatalogItemDetailsRepository;

public class CatalogItemDetailsViewModel extends StatefulViewModel {

    // Live Data
    private MutableLiveData<CatalogItemDetails> catalogItemDetails;
    private CatalogItemDetailsRepository catalogItemDetailsRepository;

    private Product product;

    public CatalogItemDetailsViewModel() {
        this.catalogItemDetailsRepository = new CatalogItemDetailsRepository();

        catalogItemDetails = new MutableLiveData<>();
    }

    public LiveData<CatalogItemDetails> getObservableCatalogItemDetails() {
        return catalogItemDetails;
    }

    public Product getProduct() {
        return product;
    }

    public void setCatalogItem(CatalogItem catalogItem) {
        this.product = catalogItem;
        updateCatalog(catalogItem);
    }

    private CatalogItemDetailsCallback catalogSearchCallback = new CatalogItemDetailsCallback() {
        @Override
        public void onCatalogItemDetailsSuccess(CatalogItemDetails catalog) {
            status.setValue(State.SUCCESS);
            catalogItemDetails.postValue(catalog);
        }

        @Override
        public void onCatalogItemDetailsError(Error error) {
            status.setValue(State.ERROR);
        }
    };

    private void updateCatalog(CatalogItem catalogItem) {
        status.setValue(State.LOADING);
        catalogItemDetailsRepository.fetchDetails(catalogItem, catalogSearchCallback);
    }
}