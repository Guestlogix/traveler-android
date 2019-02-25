package com.guestlogix.traveler_stripe_payment_provider;

import android.content.Context;
import android.content.Intent;
import com.guestlogix.traveleruikit.PaymentProvider;

public class StripePaymentProvider implements PaymentProvider {
    public Intent getPaymentActivityIntent(Context context) {
        return new Intent(context, PaymentCollectionActivity.class);
    }
}
