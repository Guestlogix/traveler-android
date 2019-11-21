package com.guestlogix.traveler_stripe_payment_provider;

public class PaymentSaveError extends Error {
    private String message;
    private Object root;

    PaymentSaveError(String message, Object root) {
        this.message = message;
        this.root = root;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getRoot() {
        return root;
    }
}
