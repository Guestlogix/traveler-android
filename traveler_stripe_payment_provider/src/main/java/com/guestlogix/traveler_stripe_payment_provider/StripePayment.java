package com.guestlogix.traveler_stripe_payment_provider;

import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.travelercorekit.utilities.TravelerLog;
import com.stripe.android.model.Token;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StripePayment implements Payment {
    private String tokenID;
    private String lastFourDigits;
    private String expirationMonth;
    private String expirationYear;

    public StripePayment(Token token) {
        tokenID = token.getId();
        expirationMonth = token.getCard().getExpMonth().toString();
        expirationYear = token.getCard().getExpYear().toString();
        lastFourDigits = token.getCard().getLast4();
    }

    @Override
    public List<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("Credit card number", lastFourDigits));
        attributes.add(new Attribute("Expiry date",
                expirationMonth + "/" + expirationYear));
        return attributes;
    }

    @Override
    public JSONObject getSecurePayload() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", tokenID);
        } catch (JSONException je) {
            TravelerLog.e("Could not construct payload.");
            je.printStackTrace();
            return null;
        }

        return jsonObject;
    }
}
