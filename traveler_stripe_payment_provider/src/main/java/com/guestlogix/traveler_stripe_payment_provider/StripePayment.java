package com.guestlogix.traveler_stripe_payment_provider;

import android.util.Log;
import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.travelercorekit.models.Payment;
import com.stripe.android.model.Token;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StripePayment implements Payment {
    private String mTokenID;
    private String mLastFourDigits;
    private String mExpirationMonth;
    private String mExpirationYear;

    public StripePayment(Token token) {
        mTokenID = token.getId();
        mExpirationMonth = token.getCard().getExpMonth().toString();
        mExpirationYear = token.getCard().getExpYear().toString();
        mLastFourDigits = token.getCard().getLast4();
    }

    @Override
    public List<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("Credit card number", mLastFourDigits));
        attributes.add(new Attribute("Expiry date",
                mExpirationMonth + "/" + mExpirationYear));
        return attributes;
    }

    @Override
    public JSONObject getSecurePayload() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", mTokenID);
        } catch (JSONException je) {
            Log.e("StripePayment", "Could not construct payload.");
            je.printStackTrace();
            return null;
        }

        return jsonObject;
    }
}
