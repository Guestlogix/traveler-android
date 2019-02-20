package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogGroup;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.traveleruikit.repositories.CatalogWidgetRepository;

import java.util.ArrayList;
import java.util.List;

public class CatalogWidgetViewModel extends ViewModel {

    private CatalogWidgetRepository catalogWidgetRepository;
    private MutableLiveData<List<CatalogGroup>> catalogGroupList;
    private MutableLiveData<CatalogWidgetViewState> viewState = new MutableLiveData<>();

    public LiveData<CatalogWidgetViewState> getViewStateObservable() {
        return viewState;
    }

    public LiveData<List<CatalogGroup>> getGroupsObservable() {
        return catalogGroupList;
    }

    public CatalogWidgetViewModel() {
        catalogWidgetRepository = new CatalogWidgetRepository();
        this.catalogGroupList = new MutableLiveData<>();
        this.catalogGroupList.setValue(new ArrayList<>());
    }

    public void updateCatalog(CatalogQuery catalogQuery) {
        viewState.postValue(CatalogWidgetViewState.LOADING);
        viewState.setValue(CatalogWidgetViewState.LOADING);
        catalogWidgetRepository.catalogSearch(catalogQuery, catalogSearchCallback);
    }

    private CatalogSearchCallback catalogSearchCallback = new CatalogSearchCallback() {
        @Override
        public void onCatalogSearchSuccess(Catalog catalog) {
            catalogGroupList.postValue(catalog.getGroups());
            viewState.postValue(CatalogWidgetViewState.SUCCESS);
        }

        @Override
        public void onCatalogSearchError(TravelerError error) {
            viewState.postValue(CatalogWidgetViewState.ERROR);
        }
    };

    public enum CatalogWidgetViewState {
        LOADING,
        SUCCESS,
        ERROR
    }
}
