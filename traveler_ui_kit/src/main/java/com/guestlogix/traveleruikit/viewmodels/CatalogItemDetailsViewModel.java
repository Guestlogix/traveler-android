package com.guestlogix.traveleruikit.viewmodels;

import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.callbacks.CheckAvailabilityCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.repositories.CatalogItemDetailsRepository;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CatalogItemDetailsViewModel extends StatefulViewModel {

    // Live Data
    private MutableLiveData<CatalogItemDetails> catalogItemDetails;
    private MutableLiveData<List<String>> timeSlotsTransformed;
    private MutableLiveData<Calendar> selectedDate;
    private SingleLiveEvent<ActionState> actionState;
    private SingleLiveEvent<BookingContext> bookingRequest;

    private CatalogItemDetailsRepository catalogItemDetailsRepository;

    // Other fields.
    private BookingContext bookingContext;
    private List<Long> timeSlots;

    public CatalogItemDetailsViewModel() {
        this.catalogItemDetailsRepository = new CatalogItemDetailsRepository();

        catalogItemDetails = new MutableLiveData<>();
        timeSlotsTransformed = new MutableLiveData<>();
        selectedDate = new MutableLiveData<>();
        actionState = new SingleLiveEvent<>();
        bookingRequest = new SingleLiveEvent<>();
    }

    public LiveData<CatalogItemDetails> getObservableCatalogItemDetails() {
        return catalogItemDetails;
    }

    public LiveData<List<String>> getObservableTimeSlots() {
        return timeSlotsTransformed;
    }

    public LiveData<Calendar> getObservableSelectedDate() {
        return selectedDate;
    }

    public LiveData<ActionState> getObservableActionState() {
        return actionState;
    }

    public LiveData<BookingContext> getObservableBookingContext() {
        return bookingRequest;
    }

    public void setCatalogItem(CatalogItem catalogItem) {

        setBookingContext(new BookingContext(catalogItem));
        updateCatalog(catalogItem);
    }

    public void setBookingDate(Calendar bookingDate) {

        bookingContext.setSelectedDate(bookingDate.getTime());
        bookingContext.setEndDateTime(bookingDate.getTime());
        updateAvailability();
    }

    public void setBookingTime(int index) {
        bookingContext.setSelectedTime(timeSlots.get(index));
        actionState.postValue(ActionState.AVAILABLE);
    }

    public void onActionSubmit(View view) {
        if (bookingContext.getSelectedDate() == null) {
            actionState.postValue(ActionState.NOT_AVAILABLE);
        } else if (bookingContext.getTimeRequired() && bookingContext.getSelectedTime() == null) {
            actionState.postValue(ActionState.TIME_REQUIRED);
        } else {
            bookingRequest.setValue(bookingContext);
        }
    }

    public BookingContext getBookingContext() {
        return bookingContext;
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

    private CheckAvailabilityCallback checkAvailabilityCallback = new CheckAvailabilityCallback() {
        @Override
        public void onCheckAvailabilitySuccess(List<Availability> availabilityList) {

            if (availabilityList != null && !availabilityList.isEmpty()) {
                Availability item = availabilityList.get(0);

                if (item.isAvailable()) {
                    setSelectedTimes(item.getTimes());
                    return;
                }
            }

            actionState.postValue(ActionState.NOT_AVAILABLE);
        }

        @Override
        public void onCheckAvailabilityError(TravelerError error) {
            actionState.postValue(ActionState.ERROR);
        }
    };

    private void updateCatalog(CatalogItem catalogItem) {
        status.setValue(State.LOADING);
        catalogItemDetailsRepository.fetchDetails(catalogItem, catalogSearchCallback);
    }

    private void updateAvailability() {
        actionState.setValue(ActionState.LOADING);
        catalogItemDetailsRepository.fetchAvailability(bookingContext, checkAvailabilityCallback);
    }

    private void setBookingContext(BookingContext bookingContext) {
        this.bookingContext = bookingContext;
    }

    private void setSelectedTimes(List<Long> times) {
        this.timeSlots = times;

        // Transform times for UI
        List<String> timesTransform = new ArrayList<>();

        for (Long item : this.timeSlots) {
            timesTransform.add(DateHelper.formatTime(item));
        }

        timeSlotsTransformed.postValue(timesTransform);

        bookingContext.setTimeRequired(times != null && !times.isEmpty());

        if (!bookingContext.getTimeRequired()) {
            actionState.postValue(ActionState.AVAILABLE);
        }
    }

    public enum ActionState {
        LOADING,
        AVAILABLE,
        NOT_AVAILABLE,
        TIME_REQUIRED,
        ERROR,
    }
}