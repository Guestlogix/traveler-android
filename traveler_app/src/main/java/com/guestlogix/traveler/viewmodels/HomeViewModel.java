package com.guestlogix.traveler.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.guestlogix.traveler.callbacks.ProfileCallback;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.*;

public class HomeViewModel extends StatefulViewModel implements ProfileCallback {
    public static final int ADD_FLIGHT_REQUEST_CODE = 1;
    public static final int REQUEST_CODE_SIGN_IN = 2;

    public static final String EXTRA_FLIGHT = "extra_flight";
    private MutableLiveData<List<Flight>> flightsList;
    private MutableLiveData<Profile> profile;
    private MutableLiveData<Catalog> catalog;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.flightsList = new MutableLiveData<>();
        this.flightsList.setValue(new ArrayList<>());
        this.profile = new MutableLiveData<>();
        this.catalog = new MutableLiveData<>();

        fetchCatalog();
    }

    public LiveData<List<Flight>> getObservableFlights() {
        return flightsList;
    }

    public LiveData<Profile> getObservableProfile() {
        return profile;
    }

    public Catalog getCatalog() {
        return catalog.getValue();
    }

    public void addFlight(Flight flight) {
        if (null != flightsList.getValue()) {
            List<Flight> flightsList = this.flightsList.getValue();
            flightsList.add(flight);
            this.flightsList.postValue(flightsList);
        } else {
            ArrayList<Flight> flightsList = new ArrayList<>();
            flightsList.add(flight);
            this.flightsList.postValue(flightsList);
        }

        fetchCatalog();
    }

    public void deleteFlight(int index) {
        if (null != flightsList.getValue()) {
            List<Flight> flightsList = this.flightsList.getValue();
            flightsList.remove(index);
            this.flightsList.postValue(flightsList);
        }

        fetchCatalog();
    }

    public void setProfile(Profile profile) {
        Guest.getInstance().setUserProfile(getApplication(), profile);
        this.profile.postValue(profile);
    }

    public void lookupProfile() {
        Profile profile = Guest.getInstance().getUserProfile(getApplication());
        if (null == profile) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplication());
            if (null != account) {
                // User already authorized us, get user model from backend
                fetchProfile(account.getIdToken());
            }
        } else {
            setProfile(profile);
        }
    }

    public void fetchProfile(String idToken) {
        Guest.getInstance().fetchProfile(idToken, this);
    }

    public void fetchCatalog() {
        status.postValue(LOADING);
        CatalogQuery catalogQuery = new CatalogQuery(flightsList.getValue());
        Traveler.fetchCatalog(catalogQuery, catalogSearchCallback);
    }

    private CatalogSearchCallback catalogSearchCallback = new CatalogSearchCallback() {
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

    // Profile Callback

    @Override
    public void onProfileReceived(Profile profile) {
        setProfile(profile);
    }

    @Override
    public void onProfileError(Error error) {
        setProfile(null);
    }
}
