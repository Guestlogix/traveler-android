package com.guestlogix.traveler.viewmodels;

import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.traveler.repositories.FlightSearchRepository;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;

import java.util.ArrayList;

public class FlightSearchViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Flight>> flightsArrayList;
    private FlightSearchRepository flightSearchRepository;

    public FlightSearchViewModel() {
        this.flightsArrayList = new MutableLiveData<>();
        this.flightSearchRepository = new FlightSearchRepository();
    }

    public LiveData<ArrayList<Flight>> getFlightsObservAble() {
        return flightsArrayList;
    }

    public void flightSearch(FlightQuery query) {
        flightSearchRepository.flightSearch(query, flightSearchCallback);
    }

    FlightSearchCallback flightSearchCallback = new FlightSearchCallback() {
        @Override
        public void onFlightSearchSuccess(ArrayList<Flight> flights) {
            flightsArrayList.setValue(flights);
        }

        @Override
        public void onFlightSearchError(TravelerError error) {
            Log.e("FlightSearchViewModel", error.toString());
        }
    };
}
