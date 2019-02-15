package com.guestlogix.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.traveleruikit.repositories.SupplierQuestionsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingViewModel extends StatefulViewModel {
    private static final String TAG = "SupplierQuestionsVM";

    private BookingContext bookingContext;
    private SupplierQuestionsRepository supplierQuestionsRepository;
    private MutableLiveData<List<Pass>> passesLiveData;
    private MutableLiveData<Double> priceChange;
    private Map<Pass, Integer> passQuantityMap;


    public BookingViewModel() {
        supplierQuestionsRepository = new SupplierQuestionsRepository();
        passesLiveData = new MutableLiveData<>();
        priceChange = new MutableLiveData<>();
    }

    public void setBookingContext(BookingContext bookingContext) {
        this.bookingContext = bookingContext;
        updatePass(this.bookingContext);
    }

    public LiveData<List<Pass>> getPassesObservable() {
        return passesLiveData;
    }

    public LiveData<Double> getPriceChangeObservable() {
        return priceChange;
    }

    public int getPassQuantity(Pass pass) {
        Integer quantity = passQuantityMap.get(pass);

        if (quantity == null) {
            quantity = 0;
        }

        return quantity;
    }

    public void updateValueForPass(int passId, int newQuantity) {
        if (passesLiveData.getValue() == null) {
            Log.w(TAG, "updateValueForPass - Passes are not initialized yet");
            return;
        }

        if (passId >= passesLiveData.getValue().size()) {
            Log.w(TAG, String.format("updateValueForPass - Incorrect passId: %d", passId));
            return;
        }

        Pass p = passesLiveData.getValue().get(passId);
        passQuantityMap.put(p, newQuantity);
        calculateTotalPrice();
    }

    private void updatePass(BookingContext bookingContext) {
        status.setValue(State.LOADING);
        supplierQuestionsRepository.fetchPasses(bookingContext, fetchPassesCallback);
    }

    private void calculateTotalPrice() {
        double price = 0.0;

        for (Map.Entry<Pass, Integer> e : passQuantityMap.entrySet()) {
            Integer quantity = e.getValue();
            Pass pass = e.getKey();

            // Prevents NPE
            if (quantity == null || quantity < 0) {
                quantity = 0;
            }

            price += (quantity * pass.getPrice().getValue());
        }

        priceChange.postValue(price);
    }

    FetchPassesCallback fetchPassesCallback = new FetchPassesCallback() {
        @Override
        public void onSuccess(List<Pass> passes) {
            Log.v(TAG, String.format("Fetched %d passes. Building all mappings", passes.size()));
            passQuantityMap = new HashMap<>();

            // Initialize all quantities to 0.
            for (Pass pass : passes) {
                passQuantityMap.put(pass, 0);
            }

            calculateTotalPrice();
            passesLiveData.postValue(passes);
            status.postValue(State.SUCCESS);
        }

        @Override
        public void onError(TravelerError error) {
            Log.d(TAG, "Error occurred while fetching passes.");
            status.setValue(State.ERROR);
        }
    };
}
