package com.guestlogix.traveler.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.traveler.repositories.FlightSearchRepository;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;

import java.util.ArrayList;

public class FlightSearchResultViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Flight>> flightsArrayList;
    private FlightSearchRepository flightSearchRepository;

    public FlightSearchResultViewModel() {
        this.flightsArrayList = new MutableLiveData<>();
        this.flightSearchRepository = new FlightSearchRepository();
    }

    public LiveData<ArrayList<Flight>> getFlightsObservable() {
        return flightsArrayList;
    }

    public void flightSearch(FlightQuery query) {
        flightSearchRepository.flightSearch(query, flightSearchCallback);
    }

    FlightSearchCallback flightSearchCallback = new FlightSearchCallback() {
        @Override
        public void onFlightSearchSuccess(ArrayList<Flight> flights) {
            Log.v("FlightSearch", "onFlightSearchSuccess()");
            flightsArrayList.setValue(flights);
        }

        @Override
        public void onFlightSearchError(Error error) {
            Log.v("FlightSearch", "onFlightSearchError()");

        }
    };
}
