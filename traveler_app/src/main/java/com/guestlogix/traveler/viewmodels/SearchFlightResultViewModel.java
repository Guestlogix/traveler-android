package com.guestlogix.traveler.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.traveler.repositories.FlightSearchRepository;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

import java.util.List;

public class SearchFlightResultViewModel extends ViewModel {
    private MutableLiveData<List<Flight>> flightsList;
    private SingleLiveEvent<FlightSearchState> flightSearchState = new SingleLiveEvent<>();
    private FlightSearchRepository flightSearchRepository;

    public SearchFlightResultViewModel() {
        this.flightsList = new MutableLiveData<>();
        this.flightSearchRepository = new FlightSearchRepository();
    }

    public LiveData<List<Flight>> getObservableFlights() {
        return flightsList;
    }
    public SingleLiveEvent<FlightSearchState> getFlightSearchState() {
        return flightSearchState;
    }

    public void flightSearch(FlightQuery query) {
        flightSearchState.setValue(FlightSearchState.LOADING);
        flightSearchRepository.flightSearch(query, flightSearchCallback);
    }

    private FlightSearchCallback flightSearchCallback = new FlightSearchCallback() {
        @Override
        public void onFlightSearchSuccess(List<Flight> flights) {
            flightSearchState.setValue(FlightSearchState.SUCCESS);
            flightsList.postValue(flights);
        }

        @Override
        public void onFlightSearchError(TravelerError error) {
            flightSearchState.setValue(FlightSearchState.ERROR);
        }
    };

    public enum FlightSearchState {
        LOADING,
        SUCCESS,
        ERROR
    }
}
