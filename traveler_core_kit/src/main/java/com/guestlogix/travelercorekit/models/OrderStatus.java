package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public abstract class OrderStatus implements Serializable {
    // This class is setup this way to be more transportable to Kotlin
    public enum Code {
        PENDING, CONFIRMED, DECLINED, UNDER_REVIEW, CANCELLED
    }

    private Code code;

    OrderStatus(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    // Statuses

    public static class Pending extends OrderStatus {
        Pending() {
            super(Code.PENDING);
        }
    }

    public static class Confirmed extends OrderStatus {
        private PaymentInfo paymentInfo;

        Confirmed(PaymentInfo paymentInfo) {
            super(Code.CONFIRMED);
            this.paymentInfo = paymentInfo;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }
    }

    public static class Declined extends OrderStatus {
        private PaymentInfo paymentInfo;

        Declined(PaymentInfo paymentInfo) {
            super(Code.DECLINED);
            this.paymentInfo = paymentInfo;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }
    }

    public static class UnderReview extends OrderStatus {
        private PaymentInfo paymentInfo;

        UnderReview(PaymentInfo paymentInfo) {
            super(Code.UNDER_REVIEW);
            this.paymentInfo = paymentInfo;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }
    }

    public static class Cancelled extends OrderStatus {
        private PaymentInfo paymentInfo;

        Cancelled(PaymentInfo paymentInfo) {
            super(Code.CANCELLED);
            this.paymentInfo = paymentInfo;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }
    }
}
