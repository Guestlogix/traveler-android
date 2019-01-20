package com.guestlogix.viewmodels;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.callbacks.CheckAvailabilityCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.traveleruikit.repositories.CatalogItemDetailsRepository;

import java.util.ArrayList;
import java.util.Calendar;

public class CatalogItemDetailsViewModel extends StatefulViewModel {
    private MutableLiveData<CatalogItemDetails> catalogItemDetails = new MutableLiveData<>();
    private MutableLiveData<CatalogItem> catalogItem = new MutableLiveData<>();
    private CatalogItemDetailsRepository catalogItemDetailsRepository;
    private MutableLiveData<Calendar> myCalendar = new MutableLiveData<>();
    private MutableLiveData<BookingContext> bookingContext = new MutableLiveData<>();


    public CatalogItemDetailsViewModel() {
        this.catalogItemDetailsRepository = new CatalogItemDetailsRepository();
    }

    public MutableLiveData<CatalogItemDetails> getCatalogItemDetailsObservable() {
        return catalogItemDetails;
    }

    public void setCatalogItemDetails(CatalogItemDetails catalogItemDetails) {
        this.catalogItemDetails.setValue(catalogItemDetails);
    }

    public MutableLiveData<CatalogItem> getCatalogItem() {
        return catalogItem;
    }

    public void setBookingContext(BookingContext bookingContext) {
        this.bookingContext.setValue(bookingContext);
    }

    public void setCatalogItem(CatalogItem catalogItem) {
        this.catalogItem.setValue(catalogItem);
        setBookingContext(new BookingContext(catalogItem));
        setMyCalendar(Calendar.getInstance());

    }

    public void setMyCalendar(Calendar myCalendar) {
        this.myCalendar.setValue(myCalendar);

        bookingContext.getValue().setStartDateTime(myCalendar.getTime());
        bookingContext.getValue().setEndDateTime(myCalendar.getTime());
    }

    public MutableLiveData<Calendar> getMyCalendarObservable() {
        return myCalendar;
    }

    public Calendar getMyCalendar() {
        return myCalendar.getValue();
    }

    public void updateCatalog(CatalogItem catalogItem) {
        status.setValue(State.LOADING);
        catalogItemDetailsRepository.fetchDetails(catalogItem, catalogSearchCallback);
    }

    private CatalogItemDetailsCallback catalogSearchCallback = new CatalogItemDetailsCallback() {
        @Override
        public void onCatalogItemDetailsSuccess(CatalogItemDetails catalog) {
            status.setValue(State.SUCCESS);
            catalogItemDetails.postValue(catalog);
        }

        @Override
        public void onCatalogItemDetailsError(TravelerError error) {
            status.setValue(State.ERROR);
        }
    };

    CheckAvailabilityCallback checkAvailabilityCallback = new CheckAvailabilityCallback() {
        @Override
        public void onCheckAvailabilitySuccess(ArrayList<Availability> availability) {
            if(availability.size()>0){
            Log.d("CatalogItemDetailsVM", "onCheckAvailabilitySuccess: Available:" + availability.get(0).getAvailable());
            }else{
                Log.d("CatalogItemDetailsVM", "onCheckAvailabilitySuccess: Not Available" );

            }
        }

        @Override
        public void onCheckAvailabilityError(TravelerError error) {
            Log.d("CatalogItemDetailsVM", "onCheckAvailabilityError: ");
        }
    };


    public void checkAvailability() {
        Traveler.checkAvailability(bookingContext.getValue(), checkAvailabilityCallback);

    }
}