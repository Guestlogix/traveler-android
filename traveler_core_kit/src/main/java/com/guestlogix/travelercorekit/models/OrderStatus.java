package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public abstract class OrderStatus implements Serializable {


    // This class is setup this way to be more transportable to Kotlin
    public enum Code {
        PENDING, CONFIRMED, DECLINED, UNDER_REVIEW, CANCELLED, UNKNOWN
    }

    private Code code;
    private String text;

    OrderStatus(Code code, String text) {
        this.code = code;
        this.text = text;
    }

    public Code getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    // Statuses

    public static class Pending extends OrderStatus {
        Pending(String text) {
            super(Code.PENDING, text);
        }
    }

    public static class Confirmed extends OrderStatus {
        private PaymentInfo paymentInfo;

        Confirmed(String text, PaymentInfo paymentInfo) {
            super(Code.CONFIRMED, text);
            this.paymentInfo = paymentInfo;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }
    }

    public static class Declined extends OrderStatus {
        private PaymentInfo paymentInfo;

        Declined(String text,PaymentInfo paymentInfo) {
            super(Code.DECLINED, text);
            this.paymentInfo = paymentInfo;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }
    }

    public static class UnderReview extends OrderStatus {
        private PaymentInfo paymentInfo;

        UnderReview(String text,PaymentInfo paymentInfo) {
            super(Code.UNDER_REVIEW, text);
            this.paymentInfo = paymentInfo;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }
    }

    public static class Cancelled extends OrderStatus {
        private PaymentInfo paymentInfo;

        Cancelled(String text,PaymentInfo paymentInfo) {
            super(Code.CANCELLED, text);
            this.paymentInfo = paymentInfo;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }
    }

    public static class Unknown extends OrderStatus {
        Unknown(String text) {
            super(Code.UNKNOWN, text);
        }
    }
}
