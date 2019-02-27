package com.guestlogix.traveler.viewmodels;

import android.content.Intent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CatalogViewModel extends ViewModel {
    public static final int ADD_FLIGHT_REQUEST_CODE = 1;
    public static final String EXTRA_FLIGHT = "extra_flight";
    private MutableLiveData<List<Flight>> flightsList;

    public CatalogViewModel() {
        this.flightsList = new MutableLiveData<>();
        this.flightsList.setValue(new ArrayList<>());
    }

    public LiveData<List<Flight>> getObservableFlights() {
        return flightsList;
    }

    public void addFlight(Flight flight) {
        //TODO User repository to manage flights in user session
        if (null != flightsList.getValue()) {
            List<Flight> flightsList = this.flightsList.getValue();
            flightsList.add(flight);
            this.flightsList.postValue(flightsList);
        } else {
            ArrayList<Flight> flightsList = new ArrayList<>();
            flightsList.add(flight);
            this.flightsList.postValue(flightsList);
        }
    }

    public void deleteFlight(int index) {
        if (null != flightsList.getValue()) {
            List<Flight> flightsList = this.flightsList.getValue();
            flightsList.remove(index);
            this.flightsList.postValue(flightsList);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_FLIGHT_REQUEST_CODE:
                    Flight flight = (Flight) data.getExtras().getSerializable(EXTRA_FLIGHT);
                    addFlight(flight);
                    break;
            }
        }
    }
}
