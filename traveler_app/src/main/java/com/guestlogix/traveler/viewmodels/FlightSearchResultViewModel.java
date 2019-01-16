package com.guestlogix.traveler.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.traveler.repositories.FlightSearchRepository;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;

import java.util.ArrayList;
import java.util.List;

public class FlightSearchResultViewModel extends ViewModel {
    private static final String TAG = "FlightSearch";

    private MutableLiveData<List<Flight>> flightsArrayList;
    private FlightSearchRepository flightSearchRepository;

    public FlightSearchResultViewModel() {
        this.flightsArrayList = new MutableLiveData<>();
        this.flightSearchRepository = new FlightSearchRepository();
    }

    public LiveData<List<Flight>> getFlightsObservable() {
        return flightsArrayList;
    }

    public void flightSearch(FlightQuery query) {
        flightSearchRepository.flightSearch(query, flightSearchCallback);
    }

    FlightSearchCallback flightSearchCallback = new FlightSearchCallback() {
        @Override
        public void onFlightSearchSuccess(List<Flight> flights) {
            Log.v("FlightSearch", "onFlightSearchSuccess()");
            flightsArrayList.postValue(flights);
        }

        @Override
        public void onFlightSearchError(TravelerError error) {
            Log.e(TAG, "onFlightSearchError");
            //TODO: Handle ERROR correctly.
        }
    };
}
