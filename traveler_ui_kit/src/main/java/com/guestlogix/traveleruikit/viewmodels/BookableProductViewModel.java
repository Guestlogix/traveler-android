package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.FetchAvailabilitiesCallback;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.models.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookableProductViewModel extends ProductViewModel implements FetchAvailabilitiesCallback, FetchPassesCallback {
    private Product product;

    private MutableLiveData<State> actionState;
    private MutableLiveData<Price> price;

    private MutableLiveData<List<Pass>> passes;
    private MutableLiveData<List<String>> options;
    private MutableLiveData<String> optionsTitle;

    private Availability currentAvailability;
    private BookingOption currentOption;

    public BookableProductViewModel() {
        actionState = new MutableLiveData<>();
        price = new MutableLiveData<>();
        passes = new MutableLiveData<>();
        options = new MutableLiveData<>();
        optionsTitle = new MutableLiveData<>();

        actionState.postValue(State.DEFAULT);
    }

    public LiveData<State> getAvailabilityState() {
        return actionState;
    }

    public LiveData<Price> getObservablePrice() {
        return price;
    }

    public LiveData<List<Pass>> getObservablePasses() {
        return passes;
    }

    public LiveData<List<String>> getObservableOptions() {
        return options;
    }

    public LiveData<String> getObservableOptionsTitle() {
        return optionsTitle;
    }

    @Override
    public void setup(Product product) {
        this.product = product;
        price.setValue(product.getPrice());
    }

    public Product getProduct() {
        return product;
    }

    public void onDateChanged(Calendar calendar) {
        actionState.setValue(State.LOADING);
        Traveler.fetchAvailabilities(product, calendar.getTime(), calendar.getTime(), this);
    }

    public void onOptionChanged(int pos) {
        currentOption = currentAvailability.getBookingOptionSet().getOptions().get(pos);
        actionState.setValue(State.AVAILABLE);
    }

    public void submit() {
        // Verify I have an availability and that I have an option if it is required.
        if (currentAvailability == null) {
            return;
        }
        //Verify if user has selected an option if there were any return otherwise.
        if (currentAvailability.getBookingOptionSet() != null) {
            if (currentAvailability.getBookingOptionSet().getOptions() != null &&
                    !currentAvailability.getBookingOptionSet().getOptions().isEmpty() && currentOption == null) {
                return;
            }
        }
        actionState.setValue(State.LOADING);
        Traveler.fetchPasses(product, currentAvailability, currentOption, this);
    }

    @Override
    public void onAvailabilitySuccess(List<Availability> availabilities) {
        // Expecting a single availability.

        if (availabilities == null || availabilities.isEmpty()) {
            actionState.postValue(State.NOT_AVAILABLE);
            return;
        }

        currentAvailability = availabilities.get(0);
        currentOption = null;

        if (currentAvailability.getBookingOptionSet() == null || currentAvailability.getBookingOptionSet().getOptions() == null || currentAvailability.getBookingOptionSet().getOptions().isEmpty()) {
            actionState.postValue(State.AVAILABLE);
            return;
        }

        List<String> optionValues = new ArrayList<>();
        for (BookingOption o : currentAvailability.getBookingOptionSet().getOptions()) {
            optionValues.add(o.getValue());
        }

        optionsTitle.postValue(currentAvailability.getBookingOptionSet().getLabel());
        options.postValue(optionValues);
        actionState.postValue(State.OPTION_NEEDED);
    }

    @Override
    public void onAvailabilityError(Error error) {
        actionState.postValue(State.ERROR);
    }

    @Override
    public void onPassFetchSuccess(List<Pass> pass) {
        passes.postValue(pass);
        actionState.postValue(State.AVAILABLE);
    }

    @Override
    public void onPassFetchError(Error error) {
        actionState.postValue(State.ERROR);
    }

    public enum State {
        DEFAULT, OPTION_NEEDED, LOADING, AVAILABLE, NOT_AVAILABLE, ERROR
    }
}
