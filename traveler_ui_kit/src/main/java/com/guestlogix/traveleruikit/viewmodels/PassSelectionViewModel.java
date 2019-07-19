package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.callbacks.FetchBookingFormCallback;
import com.guestlogix.travelercorekit.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassSelectionViewModel extends ViewModel implements FetchBookingFormCallback {
    private Product product;

    private Map<Pass, Integer> passQuantities;
    private MutableLiveData<Price> price;
    private MutableLiveData<BookingForm> bookingForm;
    private MutableLiveData<List<Pass>> passes;
    private MutableLiveData<PassSelectionState> state;

    public PassSelectionViewModel() {
        passQuantities = new HashMap<>();
        price = new MutableLiveData<>();
        bookingForm = new MutableLiveData<>();
        passes = new MutableLiveData<>();
        state = new MutableLiveData<>();
    }

    public void setup(Product product, List<Pass> passes) {
        this.product = product;
        this.passes.setValue(passes);
        this.state.setValue(PassSelectionState.DEFAULT);
    }

    public int getValue(Pass p) {
        Integer value = passQuantities.get(p);

        if (value == null) {
            value = 0;
        }

        return value;
    }

    public LiveData<Price> getObservablePrice() {
        return price;
    }

    public LiveData<BookingForm> getObservableBookingForm() {
        return bookingForm;
    }

    public LiveData<List<Pass>> getObservablePasses() {
        return passes;
    }

    public LiveData<PassSelectionState> getObservableState() {
        return state;
    }

    public void updatePassQuantity(Pass p, int quantity) {
        passQuantities.put(p, quantity);
    }

    public void fetchBookingForm() {
        state.setValue(PassSelectionState.LOADING);
        List<Pass> flatPasses = new ArrayList<>();

        for (Map.Entry<Pass, Integer> entry : passQuantities.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > 0) {
                for (int i = 0; i < entry.getValue(); i++) {
                    flatPasses.add(entry.getKey());
                }
            }
        }

        Traveler.fetchBookingForm(product, flatPasses, this);
    }

    @Override
    public void onBookingFormFetchSuccess(BookingForm bookingForm) {
        PassSelectionViewModel.this.bookingForm.postValue(bookingForm);
        state.postValue(PassSelectionState.DEFAULT);
    }

    @Override
    public void onBookingFormFetchError(Error error) {
        state.postValue(PassSelectionState.ERROR);
    }

    public enum PassSelectionState {
        DEFAULT, LOADING, ERROR
    }
}
