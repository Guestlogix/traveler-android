package com.guestlogix.travelercorekit;

import android.content.Context;
import android.util.Log;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.callbacks.WishlistAddCallback;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.tasks.NetworkTaskError;
import com.guestlogix.utils.Expectation;
import com.guestlogix.travelercorekit.models.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.guestlogix.travelercorekit.BuildConfig.TRAVELER_API_KEY;
import static com.guestlogix.travelercorekit.tasks.NetworkTaskError.Code.ALREADY_WISHLISTED;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TravelerTest{
    private static final String TAG = "TravelerTest";
    private static final String TRAVELLER_ID = "eefda674-bb01-4c6c-b0fa-46f30341bf4f";
    private static final String KENSINGTON_2H_TOUR_PRODUCT_ID = "ite_hkVh9VhhvV11nk_";

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
     * This test can fail if KENSINGTON_2H_TOUR_PRODUCT_ID changes from GLX partners
     */
    @Test
    public void addWishlistTest() throws Exception {
        Traveler.identify(TRAVELLER_ID);
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId()).thenReturn(KENSINGTON_2H_TOUR_PRODUCT_ID);

        Expectation expectation = new Expectation();
        Traveler.addToWishlist(product, new WishlistAddCallback() {
            @Override
            public void onWishlistAddSuccess(Product item, CatalogItemDetails itemDetails) {
                Log.v("TravelerTest", "wishlistTestAdd::onWishlistToggleSuccess");
                expectation.fulfill();
            }

            @Override
            public void onWishlistAddError(Error error) {
                Log.v("TravelerTest", "wishlistTestAdd::onWishlistToggleError");

                switch (((NetworkTaskError) error).getCode()) {
                    case ALREADY_WISHLISTED:
                    case ITEM_UNAVAILABLE:
                        expectation.fulfill();
                        break;
                    default:
                        error.printStackTrace();
                        throw error;
                }
            }
        });

        expectation.await();
    }

    /**
     * It's not possible to mock a flight for this test, since a mocked flight would need a valid flight ID
     * The flight IDs used to call the API are constantly changing, and past flights cannot be queried.
     * Therefore a flightSearch call is chained before the catalog call.
     *
     * Makes a chain of calls:
     * - Flight Search using AC100 at the current day
     * - Catalog search based on that flight
     * - Adds to wishlist the first product of the first catalog group
     */
    @Test
    public void fullIntegrationTest() throws Exception {
        //given
        Date date = Calendar.getInstance().getTime();
        FlightQuery query = new FlightQuery("AC100", date);
        Expectation expectation = new Expectation();
        WishlistAddCallback wishlistAddCallback = new WishlistAddCallback() {
            @Override
            public void onWishlistAddSuccess(Product item, CatalogItemDetails itemDetails) {
                Log.v("TravelerTest", "fullIntegrationTest::onWishlistAddSuccess");
                expectation.fulfill();

            }

            @Override
            public void onWishlistAddError(Error error) {
                Log.e("TravelerTest", "fullIntegrationTest::onWishlistAddError");
                error.printStackTrace();
                if (error instanceof NetworkTaskError &&
                        ALREADY_WISHLISTED.equals(((NetworkTaskError) error).getCode())) {
                     expectation.fulfill();
                } else {
                    throw error;
                }
            }
        };
        CatalogSearchCallback catalogSearchCallback = new CatalogSearchCallback() {
            @Override
            public void onCatalogSuccess(Catalog catalog) {
                Log.v("TravelerTest", "fullIntegrationTest::onCatalogSuccess");
                Assert.assertTrue(catalog.getGroups().size() >= 1);
                CatalogGroup catalogGroup = catalog.getGroups().get(0);
                Assert.assertTrue(catalogGroup.getItems().size() >= 1);
                Product product = catalogGroup.getItems().get(0);

                Traveler.identify(TRAVELLER_ID);
                Traveler.addToWishlist(product, wishlistAddCallback);
            }

            @Override
            public void onCatalogError(Error error) {
                Log.e("TravelerTest", "fullIntegrationTest::onCatalogError");
                error.printStackTrace();
                throw error;
            }
        };
        FlightSearchCallback flightSearchCallback = new FlightSearchCallback() {
            @Override
            public void onFlightSearchSuccess(List<Flight> flights) {
                Log.v("TravelerTest", "fullIntegrationTest::onFlightSearchSuccess");
                Assert.assertTrue(flights.size() >= 1);

                CatalogQuery catalogQuery = new CatalogQuery(flights);
                Traveler.fetchCatalog(catalogQuery, catalogSearchCallback);
            }

            @Override
            public void onFlightSearchError(Error error) {
                Log.e("TravelerTest", "fullIntegrationTest::onFlightSearchError");
                error.printStackTrace();
                throw error;
            }
        };

        Traveler.flightSearch(query, flightSearchCallback);

        expectation.await();
    }
}
