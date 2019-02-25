package com.guestlogix.traveleruikit;

import android.content.Context;
import android.content.Intent;

public interface PaymentProvider {
    Intent getPaymentActivityIntent(Context context);
}
