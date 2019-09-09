package com.guestlogix.travelercorekit;

import android.content.Context;
import android.util.Log;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.utils.Expectation;
import com.guestlogix.travelercorekit.models.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.guestlogix.travelercorekit.BuildConfig.TRAVELER_API_KEY;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TravelerTest{
    private static final String TAG = "TravelerTest";

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

    @Test
    public void catalogTest() throws Exception {
        Expectation expectation = new Expectation();
        List<Flight> flights = new ArrayList<>();

        CatalogQuery catalogQuery = new CatalogQuery(flights);
        Traveler.fetchCatalog(catalogQuery, new CatalogSearchCallback() {
            @Override
            public void onCatalogSuccess(Catalog catalog) {
                Log.v("TravelerTest", "catalogTest::onCatalogSuccess");
                Log.v("TravelerTest", catalog.toString());
                expectation.fulfill();
            }

            @Override
            public void onCatalogError(Error error) {
                Log.e("TravelerTest", "catalogTest::onCatalogError");
                error.printStackTrace();
                throw error;
            }
        });

        expectation.await();
    }

    /**
     * It's not possible to mock a flight for this test, since a mocked flight would need a valid flight ID
     * The flight IDs used to call the API are constantly changing, and past flights cannot be queried.
     * Therefore a flightSearch call is chained before the catalog call.
     */
    @Test
    public void flightSearchWithCatalogTest() throws Exception {
        Date date = Calendar.getInstance().getTime();
        FlightQuery query = new FlightQuery("AC100", date);

        Expectation expectation = new Expectation();

        Traveler.flightSearch(query, new FlightSearchCallback() {
            @Override
            public void onFlightSearchSuccess(List<Flight> flights) {
                Log.v("TravelerTest", "flightSearchWithCatalogTest::onFlightSearchSuccess");
                Assert.assertTrue(flights.size() >= 1);

                CatalogQuery catalogQuery = new CatalogQuery(flights);
                Traveler.fetchCatalog(catalogQuery, new CatalogSearchCallback() {
                    @Override
                    public void onCatalogSuccess(Catalog catalog) {
                        Log.v("TravelerTest", "flightSearchWithCatalogTest::onCatalogSuccess");
                        Log.v("TravelerTest", catalog.toString());
                        expectation.fulfill();
                    }

                    @Override
                    public void onCatalogError(Error error) {
                        Log.e("TravelerTest", "flightSearchWithCatalogTest::onCatalogError");
                        error.printStackTrace();
                        throw error;
                    }
                });
            }

            @Override
            public void onFlightSearchError(Error error) {
                Log.e("TravelerTest", "flightSearchWithCatalogTest::onFlightSearchError");
                error.printStackTrace();
                throw error;
            }
        });

        expectation.await();
    }
}
