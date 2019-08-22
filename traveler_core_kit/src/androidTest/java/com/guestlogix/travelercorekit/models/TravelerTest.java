package com.guestlogix.travelercorekit.models;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TravelerTest{
    private static final String TRAVELER_API_KEY = "testtesttesttesttest";

    @BeforeClass
    public static void setupOnce() {
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getContext();

        Traveler.initialize(TRAVELER_API_KEY, instrumentationContext);
    }

    @Test
    public void initTravellerTest() throws InterruptedException {
        // Verify that the received data is correct.

        Date date =  Calendar.getInstance().getTime();
        FlightQuery query = new FlightQuery("AC100", date);

        while(!Traveler.isTokenReady()){
            Thread.sleep(1000);
        }

        CountDownLatch latch = new CountDownLatch(1);

        Traveler.flightSearch(query, new FlightSearchCallback() {
            @Override
            public void onFlightSearchSuccess(List<Flight> flights) {
                latch.countDown();
            }

            @Override
            public void onFlightSearchError(Error error) {
                latch.countDown();
            }
        });
        latch.await();
    }
}
