package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.callbacks.CheckAvailabilityCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.utilities.TravelerLog;
import com.guestlogix.traveleruikit.repositories.CatalogItemDetailsRepository;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CatalogItemDetailsViewModel extends StatefulViewModel {

    // Live Data
    private MutableLiveData<CatalogItemDetails> catalogItemDetails;
    private MutableLiveData<CatalogItem> catalogItem;
    private MutableLiveData<Calendar> selectedDate;
    private MutableLiveData<Long> selectedTime;
    private SingleLiveEvent<CheckAvailabilityState> availabilityStatus;
    private CatalogItemDetailsRepository catalogItemDetailsRepository;
    private MutableLiveData<List<Long>> availableTimeSlots;
    private MutableLiveData<Boolean> timeRequired;
    private MutableLiveData<BookingContext> bookingRequested;

    // Other fields.
    private BookingContext bookingContext;

    public CatalogItemDetailsViewModel() {
        this.catalogItemDetailsRepository = new CatalogItemDetailsRepository();

        catalogItemDetails = new MutableLiveData<>();
        catalogItem = new MutableLiveData<>();
        selectedDate = new MutableLiveData<>();
        selectedTime = new MutableLiveData<>();
        availabilityStatus = new SingleLiveEvent<>();
        availableTimeSlots = new MutableLiveData<>();
        timeRequired = new MutableLiveData<>();
        bookingRequested = new MutableLiveData<>();

        this.selectedTime.postValue(null);
        this.timeRequired.setValue(true);
    }

    public LiveData<CatalogItemDetails> getCatalogItemDetailsObservable() {
        return catalogItemDetails;
    }

    public LiveData<CheckAvailabilityState> getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setCatalogItem(CatalogItem catalogItem) {
        this.catalogItem.setValue(catalogItem);
        setBookingContext(new BookingContext(catalogItem));
        setSelectedDate(Calendar.getInstance());
        updateCatalog(catalogItem);
    }

    public void setSelectedDate(Calendar selectedDate) {
        this.selectedDate.postValue(selectedDate);
        this.selectedTime.postValue(null);

        bookingContext.setSelectedDate(selectedDate.getTime());
        bookingContext.setEndDateTime(selectedDate.getTime());
    }

    public void setSelectedTime(int index) {
        selectedTime.setValue(getAvailableTimeSlots().get(index));
        bookingContext.setSelectedTime(selectedTime.getValue());
    }

    public LiveData<Long> getSelectedTimeObservable() {
        return selectedTime;
    }

    public LiveData<Calendar> getSelectedDateObservable() {
        return selectedDate;
    }

    public Long getSelectedTime() {
        return selectedTime.getValue();
    }

    public Calendar getSelectedDate() {
        return selectedDate.getValue();
    }

    public LiveData<List<Long>> getAvailableTimeSlotsObservable() {
        return availableTimeSlots;
    }

    public LiveData<BookingContext> getBookingRequestObservable() {
        return bookingRequested;
    }

    public void requestBooking() {
        bookingRequested.setValue(bookingContext);
    }

    public List<Long> getAvailableTimeSlots() {
        return availableTimeSlots.getValue();
    }

    public LiveData<Boolean> getTimeRequiredObservable() {
        return timeRequired;
    }

    public Boolean getTimeRequired() {
        return timeRequired.getValue();
    }

    public void setTimeRequired(Boolean timeRequired) {
        this.timeRequired.setValue(timeRequired);
        this.bookingContext.setTimeRequired(timeRequired);
    }

    public void updateCatalog(CatalogItem catalogItem) {
        status.setValue(State.LOADING);
        catalogItemDetailsRepository.fetchDetails(catalogItem, catalogSearchCallback);
    }

    public void checkAvailability() {
        availabilityStatus.postValue(CheckAvailabilityState.LOADING);
        this.catalogItemDetailsRepository.fetchAvailability(bookingContext, checkAvailabilityCallback);
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

    CheckAvailabilityCallback checkAvailabilityCallback = new CheckAvailabilityCallback() {
        @Override
        public void onCheckAvailabilitySuccess(List<Availability> availabilityList) {
            if (availabilityList.size() > 0) {
                Availability availability = availabilityList.get(0);
                if (availability.isAvailable()) {
                    availabilityStatus.postValue(CheckAvailabilityState.AVAILABLE);
                    if (availability.getTimes().size() > 0) {
                        setTimeRequired(true);
                    } else {
                        setTimeRequired(false);
                    }
                    extractPrettyTimeSlots(availabilityList);
                } else {
                    availabilityStatus.postValue(CheckAvailabilityState.NOT_AVAILABLE);
                }
            } else {
                availabilityStatus.postValue(CheckAvailabilityState.NOT_AVAILABLE);
            }
        }

        @Override
        public void onCheckAvailabilityError(TravelerError error) {
            TravelerLog.e("Failed to check availability. Error Code: %d", error.getCode());
            availabilityStatus.postValue(CheckAvailabilityState.ERROR);
        }
    };

    private void extractPrettyTimeSlots(List<Availability> availabilityList) {
        if (availabilityList.size() > 0) {
            availableTimeSlots.postValue(availabilityList.get(0).getTimes());
        } else {
            availableTimeSlots.postValue(new ArrayList<>());
        }
    }

    private void setBookingContext(BookingContext bookingContext) {
        this.bookingContext = bookingContext;
    }

    public enum CheckAvailabilityState {
        LOADING,
        AVAILABLE,
        NOT_AVAILABLE,
        ERROR,
    }
}