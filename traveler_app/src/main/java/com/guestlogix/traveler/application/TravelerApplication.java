package com.guestlogix.traveler.application;
import android.app.Application;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler_stripe_payment_provider.StripePaymentManager;
import com.guestlogix.traveler_stripe_payment_provider.StripePaymentProvider;
import com.guestlogix.travelercorekit.models.Currency;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.TravelerUI;

public class TravelerApplication extends Application {

    public static final String PRODUCTION_AUTH_ENDPOINT = "https://9th3dtgfg3.execute-api.ca-central-1.amazonaws.com/dev";//production
    public static final String RC_AUTH_ENDPOINT = "https://hklkg7c974.execute-api.ca-central-1.amazonaws.com/dev";//rc
    public static final String DEV_AUTH_ENDPOINT = "https://r3p9qio0x7.execute-api.ca-central-1.amazonaws.com/dev";//dev

    //    public static final String PRODUCTION_ENDPOINT = "https://traveler.guestlogix.io/v1";//productio
    public static final String RC_ENDPOINT = "https://traveler.rc.guestlogix.io/v1";//rc
    public static final String DEV_ENDPOINT = "https://traveler.dev.guestlogix.io/v1";//dev

    @Override
    public void onCreate() {
        super.onCreate();

        //for production comment out all these 4 lines
        //NOTE:never push to master with any of these uncommented
        //uncomment for rc
//        TravelerPrefs.getInstance(this).save(TravelerPrefs.Key.TRAVELER_AUTH_ENDPOINT, RC_AUTH_ENDPOINT);
//        TravelerPrefs.getInstance(this).save(TravelerPrefs.Key.TRAVELER_SDK_ENDPOINT, RC_ENDPOINT);
        //uncomment for dev
//        TravelerPrefs.getInstance(this).save(TravelerPrefs.Key.TRAVELER_AUTH_ENDPOINT, DEV_AUTH_ENDPOINT);
//        TravelerPrefs.getInstance(this).save(TravelerPrefs.Key.TRAVELER_SDK_ENDPOINT, DEV_ENDPOINT);

        Traveler.initialize(BuildConfig.TRAVELER_API_KEY, getApplicationContext(), true);
        TravelerUI.initialize(new StripePaymentProvider(), Currency.USD);
        Traveler.initialize(BuildConfig.TRAVELER_API_KEY, getApplicationContext());
        TravelerUI.initialize(new StripePaymentProvider(), new StripePaymentManager(getApplicationContext()), Currency.USD);
    }
}
