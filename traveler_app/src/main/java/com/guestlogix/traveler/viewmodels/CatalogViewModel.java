package com.guestlogix.traveler.viewmodels;

import android.content.Intent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.catalogSearchCallback;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.ERROR;
import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.LOADING;
import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.SUCCESS;

public class CatalogViewModel extends StatefulViewModel {
    public static final int ADD_FLIGHT_REQUEST_CODE = 1;
    public static final String EXTRA_FLIGHT = "extra_flight";
    private MutableLiveData<List<Flight>> flightsList;
    private MutableLiveData<Catalog> catalog;


    public CatalogViewModel() {
        this.flightsList = new MutableLiveData<>();
        this.flightsList.setValue(new ArrayList<>());
        this.catalog = new MutableLiveData<>();
    }

    public Catalog getCatalog() {
        return catalog.getValue();
    }

    public LiveData<List<Flight>> getObservableFlights() {
        return flightsList;
    }

    private void addFlight(Flight flight) {
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

    public void fetchCatalog() {
        status.postValue(LOADING);
        CatalogQuery catalogQuery = new CatalogQuery(flightsList.getValue());
        Traveler.fetchCatalog(catalogQuery, catalogSearchCallback);
    }

    private catalogSearchCallback catalogSearchCallback = new catalogSearchCallback() {
        @Override
        public void onCatalogSuccess(Catalog cat) {
            catalog.postValue(cat);
            status.postValue(SUCCESS);
        }

        @Override
        public void onCatalogError(Error error) {
            status.postValue(ERROR);
        }
    };

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
//
//    public void refreshCatalog() {
//        fetchCatalog(flightsList.getValue());
//    }
}
