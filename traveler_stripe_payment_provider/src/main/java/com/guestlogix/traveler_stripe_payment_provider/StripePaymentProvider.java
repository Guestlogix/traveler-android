package com.guestlogix.traveler_stripe_payment_provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.guestlogix.travelercorekit.callbacks.PaymentConfirmationCallback;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.travelercorekit.models.PaymentError;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.PaymentProvider;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;

public class StripePaymentProvider implements PaymentProvider {
    private static String TAG = "StripePaymentProvider";

    public Intent getPaymentActivityIntent(Context context) {
        return new Intent(context, PaymentCollectionActivity.class);
    }

    @Override
    public void confirmPayment(String errorData, Activity activity) {
        getStripeInstance(activity.getApplicationContext()).authenticatePayment(activity, errorData);
    }

    @Override
    public void confirmPayment(String errorData, Fragment fragment) {
        if (fragment.getContext() == null) {
            Log.e(TAG, "Fragment not attached to a context");
            return;
        }

        getStripeInstance(fragment.getContext().getApplicationContext()).authenticatePayment(fragment, errorData);
    }

    @Override
    public void onPaymentConfirmationActivityResult(Activity activity, int requestCode, Intent data, PaymentConfirmationCallback callback) {
        getStripeInstance(activity).onPaymentResult(requestCode, data, new ApiResultCallback<PaymentIntentResult>() {
            @Override
            public void onSuccess(@NonNull PaymentIntentResult result) {
                callback.onPaymentConfirmationSuccess();
            }

            @Override
            public void onError(@NonNull Exception e) {
                callback.onPaymentConfirmationError(new PaymentError(PaymentError.Code.CONFIRMATION_FAILED, e.getLocalizedMessage()));
            }
        });
    }

    private Stripe stripeInstance = null;
    private Stripe getStripeInstance(Context context) {
        if (stripeInstance != null)
            return stripeInstance;

        stripeInstance = new Stripe(context, Traveler.isSandboxMode() ?
                BuildConfig.STRIPE_PUBLISHABLE_KEY_DEBUG : BuildConfig.STRIPE_PUBLISHABLE_KEY_RELEASE);
        return stripeInstance;
    }
}
