package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.callbacks.ProcessOrderCallback;
import com.guestlogix.travelercorekit.models.*;

import java.util.ArrayList;
import java.util.List;

public class OrderSummaryViewModel extends ViewModel {
    private Order order;
    private Payment payment;

    private MutableLiveData<Price> displayPrice;
    private MutableLiveData<List<Product>> products;
    private MutableLiveData<State> state;
    private MutableLiveData<List<Payment>> availablePayments;
    private MutableLiveData<Receipt> receipt;

    public OrderSummaryViewModel() {
        displayPrice = new MutableLiveData<>();
        products = new MutableLiveData<>();
        state = new MutableLiveData<>();
        availablePayments = new MutableLiveData<>();
        receipt = new MutableLiveData<>();
        state.setValue(State.DEFAULT);
    }

    public void setup(Order order) {
        this.order = order;

        displayPrice.setValue(order.getTotal());
        products.setValue(order.getProducts());
    }

    public void setPayment(Payment payment) {
        this.payment = payment;

        if (payment != null) {
            // TODO: Make sure there are not repeats.
            List<Payment> payments = availablePayments.getValue();

            if (payments == null) {
                payments = new ArrayList<>();
            }

            payments.add(payment);
            availablePayments.setValue(payments);
        }

        if (payment != null) {
            state.setValue(State.READY);
        }
    }

    public void submit() {
        state.setValue(State.LOADING);
        if (payment != null) {
            Traveler.processOrder(order, payment, new ProcessOrderCallback() {
                @Override
                public void onOrderProcessSuccess(Receipt receipt) {
                    state.postValue(State.READY);
                    OrderSummaryViewModel.this.receipt.postValue(receipt);
                }

                @Override
                public void onOrderProcessError(Error error) {
                    state.postValue(State.READY);
                }
            });
        }
    }

    public LiveData<Price> getDisplayPrice() {
        return displayPrice;
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<State> getState() {
        return state;
    }

    public LiveData<List<Payment>> getAvailablePayments() {
        return availablePayments;
    }

    public LiveData<Receipt> getReceipt() {
        return receipt;
    }

    public enum State {
        DEFAULT, LOADING, READY, ERROR
    }
}
