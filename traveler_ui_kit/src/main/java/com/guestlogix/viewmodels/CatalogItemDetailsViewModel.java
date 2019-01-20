package com.guestlogix.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.traveleruikit.repositories.CatalogItemDetailsRepository;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

public class CatalogItemDetailsViewModel extends StatefulViewModel {
    private MutableLiveData<CatalogItemDetails> catalogItemDetails;
    private CatalogItemDetailsRepository catalogItemDetailsRepository;

    public CatalogItemDetailsViewModel() {
        this.catalogItemDetailsRepository = new CatalogItemDetailsRepository();
        this.catalogItemDetails = new MutableLiveData<>();
    }

    public MutableLiveData<CatalogItemDetails> getCatalogItemDetailsObservable() {
        return catalogItemDetails;
    }

    public void updateCatalog(CatalogItem catalogItem) {
        status.setValue(State.LOADING);
        catalogItemDetailsRepository.fetchDetails(catalogItem, catalogSearchCallback);
    }

    private CatalogItemDetailsCallback catalogSearchCallback = new CatalogItemDetailsCallback() {
        @Override
        public void onCatalogItemDetailsSuccess(CatalogItemDetails catalog) {
            status.setValue(State.SUCCESS);
            catalogItemDetails.postValue(catalog);
        }

        @Override
        public void onCatalogItemDetailsError(TravelerError error) {
            status.setValue(State.ERROR);
        }
    };


}