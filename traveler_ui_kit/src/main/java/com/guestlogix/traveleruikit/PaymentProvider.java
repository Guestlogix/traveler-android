package com.guestlogix.traveleruikit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.guestlogix.travelercorekit.callbacks.PaymentConfirmationCallback;

public interface PaymentProvider {
    Intent getPaymentActivityIntent(Context context);
    void confirmPayment(String errorData, Activity activity);
    void confirmPayment(String errorData, Fragment fragment);
    void onPaymentConfirmationActivityResult(Activity activity, int requestCode, Intent data, PaymentConfirmationCallback callback);
}
