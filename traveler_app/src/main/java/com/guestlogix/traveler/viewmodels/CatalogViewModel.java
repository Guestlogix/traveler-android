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
    private MutableLiveData<ArrayList<Flight>> flightsArrayList;
    private MutableLiveData<List<CatalogGroup>> groupList;

    private CatalogSearchRepository catalogRepository;


    public CatalogViewModel() {
        this.flightsArrayList = new MutableLiveData<>();
        this.groupList = new MutableLiveData<>();
        this.catalogRepository = new CatalogSearchRepository();
    }

    public LiveData<ArrayList<Flight>> getFlightsObservable() {
        return flightsArrayList;
    }

    public void updateCatalog(CatalogQuery catalogQuery) {
        catalogRepository.catalogSearch(catalogQuery, catalogSearchCallback);
    }

    public LiveData<List<CatalogGroup>> getGroupsObservable() {
        return groupList;
    }

    public void addFlight(Flight flight) {

        //TODO User repository to manage flights in user session

        if (null != flightsArrayList.getValue()) {
            ArrayList<Flight> flightsList = flightsArrayList.getValue();
            flightsList.add(flight);
            flightsArrayList.postValue(flightsList);
        } else {
            ArrayList<Flight> flightsList = new ArrayList<>();
            flightsList.add(flight);
            flightsArrayList.postValue(flightsList);
        }
    }

    public void deleteFlight(int index) {
        if (null != flightsArrayList.getValue()) {
            ArrayList<Flight> flightsList = flightsArrayList.getValue();
            flightsList.remove(index);
            flightsArrayList.postValue(flightsList);
        }
    }

    private CatalogSearchCallback catalogSearchCallback = new CatalogSearchCallback() {
        @Override
        public void onCatalogSearchSuccess(Catalog catalog) {
            groupList.postValue(catalog.getGroups());
        }

        @Override
        public void onCatalogSearchError(TravelerError error) {
            // TODO: Handle Error appropriately
        }
    };
}
