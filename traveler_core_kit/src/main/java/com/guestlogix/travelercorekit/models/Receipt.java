package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class Receipt implements Serializable {
    private Order order;
    private Payment payment;

    Receipt(Order order, Payment payment) {
        if (order == null) {
            throw new IllegalArgumentException("order can not be null");
        }

        if (payment == null) {
            throw new IllegalArgumentException("payment can not be null");
        }

        this.order = order;
        this.payment = payment;
    }

    public Order getOrder() {
        return order;
    }

    public Payment getPayment() {
        return payment;
    }
}
