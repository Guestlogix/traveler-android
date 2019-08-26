package com.guestlogix.travelercorekit.models;

import android.content.Context;
import android.util.Log;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void flightSearchTest() throws InterruptedException {

        Date date = Calendar.getInstance().getTime();
        FlightQuery query = new FlightQuery("AC100", date);

        CountDownLatch latch = new CountDownLatch(1);
        final List<Error> errorList = new ArrayList<>();

        Traveler.flightSearch(query, new FlightSearchCallback() {
            @Override
            public void onFlightSearchSuccess(List<Flight> flights) {
                Log.v("TravelerTest", "TravelerTest::onFlightSearchSuccess");
                for (Flight flight : flights) {
                    Log.v("TravelerTest", flight.toString());
                }

                Assert.assertTrue(flights.size() >= 1);
                latch.countDown();
            }

            @Override
            public void onFlightSearchError(Error error) {
                Log.v("TravelerTest", "TravelerTest::onFlightSearchError");
                error.printStackTrace();
                errorList.add(error);
                latch.countDown();
            }
        });

        latch.await();
        Assert.assertEquals(0, errorList.size());
    }

    @Test
    public void catalogueTest() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        List<Flight> flights = new ArrayList<>();
        final List<Error> errorList = new ArrayList<>();
        flights.add(getMockedFlight());

        CatalogQuery catalogueQuery = new CatalogQuery(flights);
        Traveler.fetchCatalog(catalogueQuery, new CatalogSearchCallback() {
            @Override
            public void onCatalogSuccess(Catalog catalog) {
                Log.v("TravelerTest", "TravelerTestflight::SearchTestWithCatalogueTest::onCatalogSuccess");
                Log.v("TravelerTest", catalog.toString());
                latch.countDown();
            }

            @Override
            public void onCatalogError(Error error) {
                Log.e("TravelerTest", "TravelerTest::flightSearchTestWithCatalogueTest::onCatalogError");
                error.printStackTrace();
                errorList.add(error);
                latch.countDown();
            }
        });

        latch.await();
        Assert.assertEquals(0, errorList.size());
    }

    private static Flight getMockedFlight() {
        Airport mockDepartureAirport = mock(Airport.class);
        when(mockDepartureAirport.getCity()).thenReturn("Toronto");
        when(mockDepartureAirport.getCode()).thenReturn("YYZ");
        when(mockDepartureAirport.getName()).thenReturn("Pearson Int'l Airport");
        Airport mockArrivalAirport = mock(Airport.class);
        when(mockArrivalAirport.getCity()).thenReturn("Hong Kong");
        when(mockArrivalAirport.getCode()).thenReturn("HKG");
        when(mockArrivalAirport.getName()).thenReturn("Chek Lap Kwok Int'l Airport");
        Date date = Calendar.getInstance().getTime();

        Flight mockFlight = mock(Flight.class);
        when(mockFlight.getId()).thenReturn("1234567890987654321");
        when(mockFlight.getNumber()).thenReturn("AC100");
        when(mockFlight.getArrivalAirport()).thenReturn(mockArrivalAirport);
        when(mockFlight.getDepartureAirport()).thenReturn(mockDepartureAirport);
        when(mockFlight.getDepartureDate()).thenReturn(date);
        when(mockFlight.getArrivalDate()).thenReturn(date);

        return mockFlight;
    }
}
