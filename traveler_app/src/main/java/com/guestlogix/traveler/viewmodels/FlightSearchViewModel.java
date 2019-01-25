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
import java.util.List;

public class FlightSearchViewModel extends ViewModel {

    private MutableLiveData<List<Flight>> flightsList;
    private FlightSearchRepository flightSearchRepository;

    public FlightSearchViewModel() {
        this.flightsList = new MutableLiveData<>();
        this.flightSearchRepository = new FlightSearchRepository();
    }

    public LiveData<List<Flight>> getFlightsObservAble() {
        return flightsList;
    }

    public void flightSearch(FlightQuery query) {
        flightSearchRepository.flightSearch(query, flightSearchCallback);
    }

    private FlightSearchCallback flightSearchCallback = new FlightSearchCallback() {
        @Override
        public void onFlightSearchSuccess(List<Flight> flights) {
            flightsList.setValue(flights);
        }

        @Override
        public void onFlightSearchError(TravelerError error) {
            // TODO: Tell the user that something went wrong
            Log.e("FlightSearchViewModel", error.toString());
        }
    };
}
