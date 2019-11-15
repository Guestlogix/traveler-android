package com.guestlogix.traveler_stripe_payment_provider;

import android.util.Log;

import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.travelercorekit.models.Payment;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.Token;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StripePayment implements Payment {
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
        paymentMethodId = paymentMethod.id;
        brand = paymentMethod.card.brand;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    @Override
    public List<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("Credit card number", lastFourDigits));
        attributes.add(new Attribute("Expiry date",
                expirationMonth + "/" + expirationYear));
        attributes.add(new Attribute("Brand", brand));
        attributes.add(new Attribute("LastFourDigits", lastFourDigits));
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
