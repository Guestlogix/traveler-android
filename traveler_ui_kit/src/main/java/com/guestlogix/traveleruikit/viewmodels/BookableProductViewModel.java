package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    private List<Long> times;

    private CheckAvailabilityCallback checkAvailabilityCallback = new CheckAvailabilityCallback() {
        @Override
        public void onAvailabilitySuccess(BookingContext bookingContext) {
            if (bookingContext.isReady()) {
                actionState.postValue(State.AVAILABLE);
            } else if (!bookingContext.hasAvailability()) {
                actionState.postValue(State.NOT_AVAILABLE);
            } else {
                // Has avail, but not ready == time required.
                times = bookingContext.getAvailableTimes();

                // Build pretty time for UI
                List<String> stringTimes = new ArrayList<>();
                for (Long l : times) {
                    stringTimes.add(DateHelper.formatTime(l));
                }
                timeSlots.postValue(stringTimes);

                actionState.postValue(State.TIME_REQUIRED);
            }
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

    @Override
    public void setup(Product product) {
        bookingContext = new BookingContext(product);
        price.setValue(product.getPrice());
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
        Traveler.checkAvailability(bookingContext, checkAvailabilityCallback);
    }

    public void onTimeChanged(int relPosition) {
        if (bookingContext == null) {
            TravelerLog.e("No product was set. Call setup() on BookableProductViewModel with the product for correct functionality.");
            return;
        }

        Long selectedTime = times.get(relPosition);
        bookingContext.setSelectedTime(selectedTime);
        actionState.setValue(State.AVAILABLE);
    }

    public enum State {
        DEFAULT, TIME_REQUIRED, LOADING, AVAILABLE, NOT_AVAILABLE, ERROR
    }
}
