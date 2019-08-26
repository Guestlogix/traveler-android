package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class PaymentInfo implements Serializable {
    private String detail;

    PaymentInfo(String detail) {
        this.detail = detail;
    }
}
