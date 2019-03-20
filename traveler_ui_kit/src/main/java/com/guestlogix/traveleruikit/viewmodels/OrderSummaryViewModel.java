package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.models.*;

import java.util.List;

public class OrderSummaryViewModel extends ViewModel {
    private Order order;
    private Payment payment;

    private MutableLiveData<Price> displayPrice;
    private MutableLiveData<List<Product>> products;

    public OrderSummaryViewModel() {
        displayPrice = new MutableLiveData<>();
        products = new MutableLiveData<>();
    }

    public void setup(Order order) {
        this.order = order;

        displayPrice.setValue(order.getTotal());
        products.setValue(order.getProducts());
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void submit() {
        if (payment != null) {
            //TODO
        }
    }

    public LiveData<Price> getDisplayPrice() {
        return displayPrice;
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
