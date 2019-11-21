package com.guestlogix.traveler_stripe_payment_provider;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.callbacks.PaymentSaveCallback;
import com.guestlogix.travelercorekit.callbacks.PaymentsFetchCallback;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.travelercorekit.models.PaymentError;
import com.guestlogix.travelercorekit.models.PaymentManager;
import com.stripe.android.CustomerSession;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.StripeError;
import com.stripe.android.model.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

public class StripePaymentManager implements PaymentManager {
    Context context;

    public StripePaymentManager(Context context) {
        this.context = context;

        PaymentConfiguration.init(BuildConfig.STRIPE_PUBLISHABLE_KEY);
    }

    @Override
    public void fetchPayments(PaymentsFetchCallback callback) {
        CustomerSession.initCustomerSession(context, new TravelerEphemeralKeyProvider(), false);
        CustomerSession.getInstance().getPaymentMethods(PaymentMethod.Type.Card, new CustomerSession.PaymentMethodsRetrievalListener() {
            @Override
            public void onPaymentMethodsRetrieved(@NonNull List<PaymentMethod> paymentMethods) {
                ArrayList<Payment> payments = new ArrayList<>();

                for (PaymentMethod method: paymentMethods) {
                    payments.add(new StripePayment(method));
                }

                callback.onPaymentsFetchSuccess(payments);
            }

            @Override
            public void onError(int errorCode, @NonNull String errorMessage, @Nullable StripeError stripeError) {
                callback.onPaymentsFetchError(new Error(errorMessage));
            }
        });
    }

    @Override
    public void savePayment(Payment payment, PaymentSaveCallback callback) {
        if (!(payment instanceof StripePayment)) {
            Log.e("StripePaymentManager", "Unknown payment type: " + payment);
            return;
        }

        CustomerSession.initCustomerSession(context, new TravelerEphemeralKeyProvider(), false);
        CustomerSession.getInstance().attachPaymentMethod(((StripePayment) payment).getPaymentMethodId(), new CustomerSession.PaymentMethodRetrievalListener() {
            @Override
            public void onPaymentMethodRetrieved(@NonNull PaymentMethod paymentMethod) {
                if (callback != null)
                    callback.onPaymentSaveSuccess();
            }

            @Override
            public void onError(int errorCode, @NonNull String errorMessage, @Nullable StripeError stripeError) {
                if (callback != null)
                    callback.onPaymentSaveError(new PaymentSaveError(errorMessage, stripeError));

                Log.w("StripePaymentManager", errorMessage);
            }
        });
    }
}
