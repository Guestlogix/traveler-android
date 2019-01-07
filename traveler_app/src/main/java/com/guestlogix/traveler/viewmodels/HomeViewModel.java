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

}
