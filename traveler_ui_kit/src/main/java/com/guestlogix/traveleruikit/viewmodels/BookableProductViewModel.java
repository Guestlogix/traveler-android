package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.CheckAvailabilityCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.TravelerLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookableProductViewModel extends ProductViewModel {
    private BookingContext bookingContext;

    private MutableLiveData<State> actionState;
    private MutableLiveData<List<String>> timeSlots;
    private MutableLiveData<Price> price;

    private Availability availability;
    private CheckAvailabilityCallback checkAvailabilityCallback = new CheckAvailabilityCallback() {
        @Override
        public void onAvailabilitySuccess(List<Availability> availabilityList) {

            if (availabilityList != null && !availabilityList.isEmpty()) {
                Availability item = availabilityList.get(0);

                if (item.isAvailable()) {
                    availability = item;
                    bookingContext.setTimeRequired(item.getTimes() != null && !item.getTimes().isEmpty());

                    // Build times.
                    if (bookingContext.getTimeRequired()) {
                        List<String> transformedTimes = new ArrayList<>();
                        for (Long time : availability.getTimes()) {
                            transformedTimes.add(DateHelper.formatTime(time));
                        }

                        timeSlots.postValue(transformedTimes);
                        actionState.postValue(State.TIME_REQUIRED);
                    } else {
                        actionState.postValue(State.AVAILABLE);
                    }
                    return;
                }
            }
            actionState.postValue(State.NOT_AVAILABLE);
        }

        @Override
        public void onAvailabilityError(TravelerError error) {
            actionState.postValue(State.ERROR);
        }
    };

    public BookableProductViewModel() {
        actionState = new MutableLiveData<>();
        timeSlots = new MutableLiveData<>();
        price = new MutableLiveData<>();

        actionState.postValue(State.DEFAULT);
    }

    public LiveData<State> getAvailabilityState() {
        return actionState;
    }

    public LiveData<List<String>> getBookingTimes() {
        return timeSlots;
    }

    public LiveData<Price> getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price.setValue(price);
    }

    @Override
    public void setup(Product product) {
        bookingContext = new BookingContext(product);
    }

    public BookingContext getBookingContext() {
        return bookingContext;
    }

    public void onDateChanged(Calendar calendar) {
        if (bookingContext == null) {
            TravelerLog.e("No product was set. Call setup() on BookableProductViewModel with the product for correct functionality.");
            return;
        }
        actionState.setValue(State.LOADING);

        bookingContext.setSelectedDate(calendar.getTime());
        bookingContext.setEndDateTime(calendar.getTime());
        Traveler.checkAvailability(bookingContext, checkAvailabilityCallback);
    }

    public void onTimeChanged(int relPosition) {
        if (bookingContext == null) {
            TravelerLog.e("No product was set. Call setup() on BookableProductViewModel with the product for correct functionality.");
            return;
        }

        Long selectedTime = availability.getTimes().get(relPosition);
        bookingContext.setSelectedTime(selectedTime);
        actionState.setValue(State.AVAILABLE);
    }

    public enum State {
        DEFAULT, TIME_REQUIRED, LOADING, AVAILABLE, NOT_AVAILABLE, ERROR
    }
}
