package com.guestlogix.travelercorekit.models;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
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
public class TravelerTest {
    private static final String TRAVELER_API_KEY = "testtesttesttesttest";

    @BeforeClass
    public static void setupOnce() {
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getContext();

        Traveler.initialize(TRAVELER_API_KEY, instrumentationContext);
    }

    @Test
    public void flightSearchTest() throws Exception {

        Date date = Calendar.getInstance().getTime();
        FlightQuery query = new FlightQuery("AC100", date);

        CountDownLatch latch = new CountDownLatch(1);
        final List<Error> errorList = new ArrayList<>();

        Traveler.flightSearch(query, new FlightSearchCallback() {
            @Override
            public void onFlightSearchSuccess(List<Flight> flights) {
                System.out.println("TravelerTest::onFlightSearchSuccess");
                for (Flight flight : flights) {
                    System.out.println(flight.toString());
                }
                latch.countDown();
            }

            @Override
            public void onFlightSearchError(Error error) {
                System.out.println("TravelerTest::onFlightSearchError");
                error.printStackTrace();
                errorList.add(error);
                latch.countDown();
            }
        });
        latch.await();
        Assert.assertEquals(0, errorList.size());
    }

    @Test
    public void flightSearchTestWithCatalogueTest() throws Exception {

        Date date = Calendar.getInstance().getTime();
        FlightQuery flightQuery = new FlightQuery("AC100", date);

        while (!Traveler.isTokenReady()) {
            Thread.sleep(1000);
        }

        CountDownLatch latch = new CountDownLatch(1);
        final List<Error> errorList = new ArrayList<>();

        Traveler.flightSearch(flightQuery, new FlightSearchCallback() {
            @Override
            public void onFlightSearchSuccess(List<Flight> flights) {
                System.out.println("TravelerTest::onFlightSearchSuccess");
                for (Flight flight : flights) {
                    System.out.println(flight.toString());
                }

                CatalogQuery catalogueQuery = new CatalogQuery(flights);
                Traveler.fetchCatalog(catalogueQuery, new CatalogSearchCallback() {
                    @Override
                    public void onCatalogSuccess(Catalog catalog) {
                        System.out.println("TravelerTestflight::SearchTestWithCatalogueTest::onCatalogSuccess");
                        System.out.println(catalog.toString());
                        latch.countDown();
                    }

                    @Override
                    public void onCatalogError(Error error) {
                        System.out.println("TravelerTest::flightSearchTestWithCatalogueTest::onCatalogError");
                        error.printStackTrace();
                        errorList.add(error);
                        latch.countDown();
                    }
                });
            }

            @Override
            public void onFlightSearchError(Error error) {
                System.out.println("TravelerTest::flightSearchTestWithCatalogueTest::onFlightSearchError");
                error.printStackTrace();
                errorList.add(error);
                latch.countDown();
            }
        });
        latch.await();
        Assert.assertEquals(0, errorList.size());
    }
}
