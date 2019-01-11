package com.guestlogix.traveler.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Flight>> flightsArrayList;

    public HomeViewModel() {
        this.flightsArrayList = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Flight>> getFlightsObservable() {
        return flightsArrayList;
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
}
