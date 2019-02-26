package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogGroup;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.traveleruikit.repositories.CatalogWidgetRepository;

import java.util.ArrayList;
import java.util.List;

import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.*;

public class CatalogWidgetViewModel extends StatefulViewModel {

    private CatalogWidgetRepository catalogWidgetRepository;
    private MutableLiveData<List<CatalogGroup>> catalogGroupList;

    public LiveData<List<CatalogGroup>> getGroupsObservable() {
        return catalogGroupList;
    }

    public CatalogWidgetViewModel() {
        catalogWidgetRepository = new CatalogWidgetRepository();
        this.catalogGroupList = new MutableLiveData<>();
        this.catalogGroupList.setValue(new ArrayList<>());
    }

    public void updateCatalog(CatalogQuery catalogQuery) {
        status.postValue(LOADING);
        catalogWidgetRepository.catalogSearch(catalogQuery, catalogSearchCallback);
    }

    private CatalogSearchCallback catalogSearchCallback = new CatalogSearchCallback() {
        @Override
        public void onCatalogSearchSuccess(Catalog catalog) {
            catalogGroupList.postValue(catalog.getGroups());
            status.postValue(SUCCESS);
        }

        @Override
        public void onCatalogSearchError(TravelerError error) {
            status.postValue(ERROR);
        }
    };

}
