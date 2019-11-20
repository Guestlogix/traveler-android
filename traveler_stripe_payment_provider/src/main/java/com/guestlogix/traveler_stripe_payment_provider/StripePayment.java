package com.guestlogix.traveler_stripe_payment_provider;

import android.util.Log;

import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.travelercorekit.models.Payment;
import com.stripe.android.model.PaymentMethod;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StripePayment implements Payment {
    public static final String CREDIT_CARD_NUMBER = "Credit card number";
    public static final String EXPIRY_DATE = "Expiry date";
    public static final String LAST_FOUR_DIGITS = "Last four digits";
    public static final String BRAND = "Brand";
    private String paymentMethodId;
    private String lastFourDigits;
    private String expirationMonth;
    private String expirationYear;
    private String brand;

    StripePayment(PaymentMethod paymentMethod) throws IllegalArgumentException {
        if (paymentMethod.card == null)
            throw new IllegalArgumentException();

        if (paymentMethod.card.expiryMonth == null || paymentMethod.card.expiryYear == null) {
            throw new IllegalArgumentException();
        }

        expirationMonth = paymentMethod.card.expiryMonth.toString();
        expirationYear = paymentMethod.card.expiryYear.toString();
        lastFourDigits = paymentMethod.card.last4;
        brand = paymentMethod.card.brand;
        paymentMethodId = paymentMethod.id;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    @Override
    public List<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute(CREDIT_CARD_NUMBER, lastFourDigits));
        attributes.add(new Attribute(EXPIRY_DATE,
                expirationMonth + "/" + expirationYear));
        attributes.add(new Attribute(BRAND, this.brand));
        attributes.add(new Attribute(LAST_FOUR_DIGITS, this.lastFourDigits));
        return attributes;
    }

    @Override
    public JSONObject getSecurePayload() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentMethodId", paymentMethodId);
        } catch (JSONException je) {
            Log.e("StripePayment", "Could not construct secure payload");
            return null;
        }

        return jsonObject;
    }
}
