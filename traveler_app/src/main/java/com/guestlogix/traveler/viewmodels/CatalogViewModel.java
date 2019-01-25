package com.guestlogix.traveler.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.traveler.repositories.CatalogSearchRepository;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.CatalogGroup;

import java.util.ArrayList;
import java.util.List;

public class CatalogViewModel extends ViewModel {
    private MutableLiveData<List<Flight>> flightsList;
    private MutableLiveData<List<CatalogGroup>> catalogGroupList;
    private CatalogSearchRepository catalogRepository;

    public CatalogViewModel() {
        this.flightsList = new MutableLiveData<>();
        this.flightsList.setValue(new ArrayList<>());
        this.catalogGroupList = new MutableLiveData<>();
        this.catalogGroupList.setValue(new ArrayList<>());
        this.catalogRepository = new CatalogSearchRepository();
    }

    public LiveData<List<Flight>> getFlightsObservable() {
        return flightsList;
    }

    public List<Flight> getFlights() {
        return flightsList.getValue();
    }

    public void updateCatalog(CatalogQuery catalogQuery) {
        catalogRepository.catalogSearch(catalogQuery, catalogSearchCallback);
    }

    public LiveData<List<CatalogGroup>> getGroupsObservable() {
        return catalogGroupList;
    }

    public List<CatalogGroup> getGroups() {
        return catalogGroupList.getValue();
    }

    public void addFlight(Flight flight) {
        //TODO User repository to manage flights in user session
        if (null != flightsList.getValue()) {
            List<Flight> flightsList = this.flightsList.getValue();
            flightsList.add(flight);
            this.flightsList.postValue(flightsList);
        } else {
            ArrayList<Flight> flightsList = new ArrayList<>();
            flightsList.add(flight);
            this.flightsList.postValue(flightsList);
        }
    }

    public void deleteFlight(int index) {
        if (null != flightsList.getValue()) {
            List<Flight> flightsList = this.flightsList.getValue();
            flightsList.remove(index);
            this.flightsList.postValue(flightsList);
        }
    }

    private CatalogSearchCallback catalogSearchCallback = new CatalogSearchCallback() {
        @Override
        public void onCatalogSearchSuccess(Catalog catalog) {
            catalogGroupList.postValue(catalog.getGroups());
        }

        @Override
        public void onCatalogSearchError(TravelerError error) {
            // TODO: Handle Error appropriately
        }
    };
}
