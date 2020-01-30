package com.guestlogix.traveler.application;
import android.app.Application;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler_stripe_payment_provider.StripePaymentManager;
import com.guestlogix.traveler_stripe_payment_provider.StripePaymentProvider;
import com.guestlogix.travelercorekit.models.Currency;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.utilities.TravelerPrefs;
import com.guestlogix.traveleruikit.TravelerUI;

public class TravelerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //TODO maybe there is a better way to pass endpoints? instead of using preferences
        TravelerPrefs.getInstance(this).save(TravelerPrefs.Key.TRAVELER_AUTH_ENDPOINT, BuildConfig.AUTH_ENDPOINT_URL);
        TravelerPrefs.getInstance(this).save(TravelerPrefs.Key.TRAVELER_SDK_ENDPOINT, BuildConfig.SDK_ENDPOINT_URL);

        Traveler.initialize(BuildConfig.TRAVELER_API_KEY, getApplicationContext(), true);
        TravelerUI.initialize(new StripePaymentProvider(), new StripePaymentManager(getApplicationContext()), Currency.USD);
    }
}
