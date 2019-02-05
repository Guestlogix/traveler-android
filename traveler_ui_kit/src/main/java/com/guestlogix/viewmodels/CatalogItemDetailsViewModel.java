package com.guestlogix.viewmodels;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.callbacks.CheckAvailabilityCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.traveleruikit.repositories.CatalogItemDetailsRepository;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CatalogItemDetailsViewModel extends StatefulViewModel {
    private MutableLiveData<CatalogItemDetails> catalogItemDetails = new MutableLiveData<>();
    private MutableLiveData<CatalogItem> catalogItem = new MutableLiveData<>();
    private MutableLiveData<Calendar> selectedDate = new MutableLiveData<>();
    private MutableLiveData<Long> selectedTime = new MutableLiveData<>();
    private MutableLiveData<BookingContext> bookingContext = new MutableLiveData<>();
    private SingleLiveEvent<CheckAvailabilityState> availabilityStatus = new SingleLiveEvent<>();
    private CatalogItemDetailsRepository catalogItemDetailsRepository;
    private MutableLiveData<List<Long>> availableTimeSlots = new MutableLiveData<>();
    private MutableLiveData<Boolean> timeRequired = new MutableLiveData<>();

    public CatalogItemDetailsViewModel() {
        this.catalogItemDetailsRepository = new CatalogItemDetailsRepository();
        this.selectedTime.postValue(null);
        this.timeRequired.setValue(true);
    }

    public MutableLiveData<CatalogItemDetails> getCatalogItemDetailsObservable() {
        return catalogItemDetails;
    }

    public SingleLiveEvent<CheckAvailabilityState> getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setBookingContext(BookingContext bookingContext) {
        this.bookingContext.setValue(bookingContext);
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

        bookingContext.getValue().setSelectedDate(selectedDate.getTime());
        bookingContext.getValue().setEndDateTime(selectedDate.getTime());
        bookingContext.postValue(bookingContext.getValue());
    }

    public void setSelectedTime(int index) {
        selectedTime.setValue(getAvailableTimeSlots().get(index));
        bookingContext.getValue().setSelectedTime(selectedTime.getValue());
    }

    public MutableLiveData<Long> getSelectedTimeObservable() {
        return selectedTime;
    }

    public MutableLiveData<Calendar> getSelectedDateObservable() {
        return selectedDate;
    }

    public Long getSelectedTime() {
        return selectedTime.getValue();
    }

    public Calendar getSelectedDate() {
        return selectedDate.getValue();
    }

    public MutableLiveData<List<Long>> getAvailableTimeSlotsObservable() {
        return availableTimeSlots;
    }

    public List<Long> getAvailableTimeSlots() {
        return availableTimeSlots.getValue();
    }

    public MutableLiveData<Boolean> getTimeRequiredObservable() {
        return timeRequired;
    }

    public Boolean getTimeRequired() {
        return timeRequired.getValue();
    }

    public void setTimeRequired(Boolean timeRequired) {
        this.timeRequired.setValue(timeRequired);
        this.bookingContext.getValue().setTimeRequired(timeRequired);
    }

    public void updateCatalog(CatalogItem catalogItem) {
        status.setValue(State.LOADING);
        catalogItemDetailsRepository.fetchDetails(catalogItem, catalogSearchCallback);
    }

    public void checkAvailability() {
        availabilityStatus.postValue(CheckAvailabilityState.LOADING);
        this.catalogItemDetailsRepository.fetchAvailability(bookingContext.getValue(), checkAvailabilityCallback);
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
                    Log.d("CatalogItemDetailsVM", "onCheckAvailabilitySuccess: Available:" + availabilityList.get(0).isAvailable());
                    availabilityStatus.postValue(CheckAvailabilityState.AVAILABLE);
                    if (availability.getTimes().size() > 0) {
                        Log.d("CatalogItemDetailsVM", "onCheckAvailabilitySuccess: Slots: " + availability.getTimes().size());
                        setTimeRequired(true);
                    } else {
                        Log.d("CatalogItemDetailsVM", "onCheckAvailabilitySuccess: No Slots");
                        setTimeRequired(false);
                    }
                    extractPrettyTimeSlots(availabilityList);
                } else {
                    Log.d("CatalogItemDetailsVM", "onCheckAvailabilitySuccess: Not Available because availability false");
                    availabilityStatus.postValue(CheckAvailabilityState.NOT_AVAILABLE);
                }
            } else {
                Log.d("CatalogItemDetailsVM", "onCheckAvailabilitySuccess: Not available because empty response");
                availabilityStatus.postValue(CheckAvailabilityState.NOT_AVAILABLE);
            }
        }

        @Override
        public void onCheckAvailabilityError(TravelerError error) {
            Log.d("CatalogItemDetailsVM", "onCheckAvailabilityError: ");
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

    public enum CheckAvailabilityState {
        LOADING,
        AVAILABLE,
        NOT_AVAILABLE,
        ERROR,
    }
}