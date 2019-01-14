package com.guestlogix.traveler.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.traveler.repositories.CatalogSearchRepository;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.Group;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Flight>> flightsArrayList;
    private MutableLiveData<List<Group>> groupList;

    private CatalogSearchRepository catalogRepository;


    public HomeViewModel() {
        this.flightsArrayList = new MutableLiveData<>();
        this.groupList = new MutableLiveData<>();
        this.catalogRepository = new CatalogSearchRepository();
    }

    public LiveData<ArrayList<Flight>> getFlightsObservable() {
        return flightsArrayList;
    }
    public LiveData<List<Group>> getGroupsObservable() {
        return groupList;
    }

    public void updateCatalog(List<String> flightIds) {
        catalogRepository.catalogSearch(flightIds, catalogSearchCallback);
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
