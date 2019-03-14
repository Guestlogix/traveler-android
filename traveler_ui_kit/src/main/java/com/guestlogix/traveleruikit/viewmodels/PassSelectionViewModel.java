package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.callbacks.FetchBookingFormCallback;
import com.guestlogix.travelercorekit.models.BookingForm;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassSelectionViewModel extends ViewModel {
    private Map<Pass, Integer> passQuantities;
    private MutableLiveData<Double> price;
    private MutableLiveData<String> currency;
    private MutableLiveData<BookingForm> bookingForm;

    public PassSelectionViewModel() {
        passQuantities = new HashMap<>();
        price = new MutableLiveData<>();
        currency = new MutableLiveData<>();
        bookingForm = new MutableLiveData<>();
    }

    public int getValue(Pass p) {
        Integer value = passQuantities.get(p);

        if (value == null) {
            value = 0;
        }

        return value;
    }

    public LiveData<Double> getPrice() {
        return price;
    }

    public LiveData<String> getCurrency() {
        return currency;
    }

    public MutableLiveData<BookingForm> getBookingForm() {
        return bookingForm;
    }

    public void updatePassQuantity(Pass p, int quantity) {
        passQuantities.put(p, quantity);

        // Calculate new price.
        double price = 0.0;
        String currency = null;
        for (Map.Entry<Pass, Integer> entry : passQuantities.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > 0) {
                price += entry.getKey().getPrice().getValue() * entry.getValue();

                if (null == currency){
                    currency = entry.getKey().getPrice().getCurrency();
                } else {
                    if (!currency.equalsIgnoreCase(entry.getKey().getPrice().getCurrency())) {
                        // TODO: handle adding different currencies.
                        return;
                    }
                }
            }
        }

        this.price.setValue(price);
        this.currency.setValue(currency);
    }

    public void fetchBookingForm(Product product) {
        List<Pass> flatPasses = new ArrayList<>();

        for (Map.Entry<Pass, Integer> entry : passQuantities.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > 0) {
                for (int i = 0; i < entry.getValue() ; i++) {
                    flatPasses.add(entry.getKey());
                }
            }
        }

        Traveler.fetchBookingForm(product, flatPasses, new FetchBookingFormCallback() {
            @Override
            public void onBookingFormFetchSuccess(BookingForm bookingForm) {
                PassSelectionViewModel.this.bookingForm.postValue(bookingForm);
            }

            @Override
            public void onBookingFormFetchError(Error error) {
                // TODO
                int x = 0;
            }
        });
    }
}
