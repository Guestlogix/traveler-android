package com.guestlogix.travelercorekit;

import android.content.Context;
import android.util.Log;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.utils.Expectation;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TravelerTest{
    private static final String TAG = "TravelerTest";
    private static final String TRAVELER_API_KEY = "testtesttesttesttest";

    @BeforeClass
    public static void setupOnce() {
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getContext();
        Traveler.initialize(TRAVELER_API_KEY, instrumentationContext);
    }

    @Test
    public void flightSearchTest() throws InterruptedException{
        Date date =  Calendar.getInstance().getTime();
        FlightQuery query = new FlightQuery("AC100", date);
        Expectation expectation = new Expectation();

        Traveler.flightSearch(query, new FlightSearchCallback() {
            @Override
            public void onFlightSearchSuccess(List<Flight> flights) {
                Log.v(TAG, "TravelerTest::onFlightSearchSuccess");
                expectation.fulfill();
            }

            @Override
            public void onFlightSearchError(Error error) {
                Log.e(TAG, "TravelerTest::onFlightSearchError");
                Log.e(TAG, error.getMessage());
                throw error;
            }
        });
        expectation.await();
    }
}
