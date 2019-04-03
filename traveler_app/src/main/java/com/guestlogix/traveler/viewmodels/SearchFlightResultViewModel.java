package com.guestlogix.traveler.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.traveler.repositories.FlightSearchRepository;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.List;

import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.*;

public class SearchFlightResultViewModel extends StatefulViewModel {
    private MutableLiveData<List<Flight>> flightsList;
    private FlightSearchRepository flightSearchRepository;

    public SearchFlightResultViewModel() {
        this.flightsList = new MutableLiveData<>();
        this.flightSearchRepository = new FlightSearchRepository();
    }

    public LiveData<List<Flight>> getObservableFlights() {
        return flightsList;
    }

    private FlightSearchCallback flightSearchCallback = new FlightSearchCallback() {
        @Override
        public void onFlightSearchSuccess(List<Flight> flights) {
            if (flights != null && !flights.isEmpty()) {
                flightsList.setValue(flights);
                status.setValue(SUCCESS);
            } else {
                status.setValue(ERROR);
            }
        }

        @Override
        public void onFlightSearchError(Error error) {
            status.setValue(ERROR);
        }
    };

    public void flightSearch(FlightQuery query) {
        status.setValue(LOADING);
        flightSearchRepository.flightSearch(query, flightSearchCallback);
    }
}
