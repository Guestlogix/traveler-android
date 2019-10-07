package com.guestlogix.travelercorekit;

import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.guestlogix.travelercorekit.callbacks.*;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
     * It's not possible to mock a flight for this test, since a mocked flight would need a valid flight ID
     * The flight IDs used to call the API are constantly changing, and past flights cannot be queried.
     * Therefore a flightSearch call is chained before the catalog call.
     *
     * Makes a chain of calls:
     * 1 Flight Search using AC100 at the current day
     * 2 Catalog search based on that flight. We will test using the first product of the first catalog group
     * 3 Query for wishlist.
     * 4 if product is in wishlist, remove it; else add the product to wishlist
     * 5 Adds to wishlist
     * 6 Queries the wishlist and asserts that the product is in wishlist
     * 7 remove product from wishlist
     * 8 Queries the wishlist and asserts that the product is NOT in wishlist
     */
    @Test
    public void fullIntegrationTest() throws Exception {
        //given
        Traveler.identify(TRAVELLER_ID);
        Date date = Calendar.getInstance().getTime();
        FlightQuery query = new FlightQuery("AC100", date);
        class StringReference { String reference;}
        class ProductReference { Product reference;}
        final StringReference productIdReference = new StringReference();
        final ProductReference  productReference = new ProductReference ();

        Expectation expectation = new Expectation();

        // 8 Queries the wishlist and asserts that the product is NOT in wishlist
        WishlistFetchCallback step8WishlistFetchCallback = new WishlistFetchCallback() {
            @Override
            public void onWishlistFetchSuccess(WishlistResult result, int identifier) {
                Log.v("TravelerTest", "fullIntegrationTest::step8WishlistFetchCallback::onWishlistQuerySuccess");
                String targetProductId = productIdReference.reference;
                Product targetProduct = null;
                for (CatalogItem item : result.getItems()) {
                    if (item.getId().equals(targetProductId)) {
                        targetProduct = item;
                        break;
                    }
                }
                Assert.assertNull(targetProduct);
                expectation.fulfill();
            }

            @Override
            public void onWishlistFetchError(Error error, int identifier) {
                Log.e("TravelerTest", "fullIntegrationTest::step8WishlistFetchCallback::onWishlistQueryError");
                error.printStackTrace();
                throw error;
            }

            @Nullable
            @Override
            public WishlistResult getPreviousResult() {
                return null;
            }

            @Override
            public void onWishlistFetchReceive(WishlistResult result, int identifier) {
                //no-op
            }
        };

        // 7 product was removed from wishlist
        WishlistRemoveCallback step7WishlistRemoveCallback= new WishlistRemoveCallback() {
            @Override
            public void onWishlistRemoveSuccess(Product item, CatalogItemDetails itemDetails) {
                Assert.assertEquals(productIdReference.reference, item.getId());
                Assert.assertEquals(productIdReference.reference, itemDetails.getId());
                Log.v("TravelerTest", "fullIntegrationTest::step7WishlistRemoveCallback::onWishlistRemoveSuccess");
                Traveler.fetchWishlist(new WishlistQuery(0, 10, new Date(), new Date()), 0, step8WishlistFetchCallback);
            }

            @Override
            public void onWishlistRemoveError(Error error, WishlistResult result) {
                Log.e("TravelerTest", "fullIntegrationTest::step7WishlistRemoveCallback::onWishlistRemoveError");
                error.printStackTrace();
                throw error;
            }
        };

        // 6 Queries the wishlist and asserts that the product is in wishlist
        WishlistFetchCallback step6WishlistFetchCallback = new WishlistFetchCallback() {
            @Override
            public void onWishlistFetchSuccess(WishlistResult result, int identifier) {
                Log.v("TravelerTest", "fullIntegrationTest::step6WishlistFetchCallback::onWishlistQuerySuccess");
                String targetProductId = productIdReference.reference;
                Product targetProduct = null;
                for (CatalogItem item : result.getItems()) {
                    if (item.getId().equals(targetProductId)) {
                        targetProduct = item;
                        break;
                    }
                }
                Assert.assertNotNull(targetProduct);

                WishlistResult immediateResult  = Traveler.wishlistRemove(targetProduct, result, step7WishlistRemoveCallback);
                Assert.assertNotNull(immediateResult);
                Assert.assertNotNull(immediateResult.getItems());
                Assert.assertFalse(immediateResult.getItems().contains(targetProduct));
            }

            @Override
            public void onWishlistFetchError(Error error, int identifier) {
                Log.e("TravelerTest", "fullIntegrationTest::step6WishlistFetchCallback::onWishlistQueryError");
                error.printStackTrace();
                throw error;
            }

            @Nullable
            @Override
            public WishlistResult getPreviousResult() {
                return null;
            }

            @Override
            public void onWishlistFetchReceive(WishlistResult result, int identifier) {
                //no-op
            }
        };

        // 5 The target item was added to wishlist
        WishlistAddCallback step5WishlistAddCallback = new WishlistAddCallback() {
            @Override
            public void onWishlistAddSuccess(Product item, CatalogItemDetails itemDetails) {
                Assert.assertTrue(productIdReference.reference.equals(itemDetails.getId()));
                Log.v("TravelerTest", "fullIntegrationTest::step5WishlistAddCallback::onWishlistAddSuccess");
                // Call chain 6
                Traveler.fetchWishlist(new WishlistQuery(0, 10, new Date(), new Date()), 0, step6WishlistFetchCallback);
            }

            @Override
            public void onWishlistAddError(Error error) {
                Log.e("TravelerTest", "fullIntegrationTest::step5WishlistAddCallback::onWishlistAddError");
                error.printStackTrace();
                throw error;
            }
        };

        // 4 product was in wishlist, it has now been removed and can be re-added
        WishlistRemoveCallback step4WishlistRemoveCallback= new WishlistRemoveCallback() {
            @Override
            public void onWishlistRemoveSuccess(Product item, CatalogItemDetails itemDetails) {
                Assert.assertEquals(productReference.reference.getId(), item.getId());
                Assert.assertEquals(productReference.reference.getId(), itemDetails.getId());
                Log.v("TravelerTest", "fullIntegrationTest::step4WishlistRemoveCallback::onWishlistRemoveSuccess");
                // Call chain 5
                Traveler.addToWishlist(productReference.reference, step5WishlistAddCallback);
            }

            @Override
            public void onWishlistRemoveError(Error error, WishlistResult result) {
                Log.e("TravelerTest", "fullIntegrationTest::step4WishlistRemoveCallback::onWishlistRemoveError");
                error.printStackTrace();
                if (error instanceof NetworkTaskError &&
                        ALREADY_WISHLISTED.equals(((NetworkTaskError) error).getCode())) {
                     expectation.fulfill();
                } else {
                    throw error;
                }
            }
        };

        // 3 Query for wishlist.
        WishlistFetchCallback step3WishlistFetchCallback = new WishlistFetchCallback() {
            @Override
            public void onWishlistFetchSuccess(WishlistResult result, int identifier) {
                Log.v("TravelerTest", "fullIntegrationTest::step3WishlistFetchCallback::onWishlistQuerySuccess");
                String targetProductId = productIdReference.reference;
                Product targetProduct = null;
                for (CatalogItem item : result.getItems()) {
                    if (item.getId().equals(targetProductId)) {
                        targetProduct = item;
                        break;
                    }
                }

                //if product is in wishlist, remove it(4); else add the product to wishlist(5)
                if (null != targetProduct) {
                    productReference.reference = targetProduct;
                    WishlistResult immediateResult = Traveler.wishlistRemove(targetProduct, result, step4WishlistRemoveCallback);
                    Assert.assertNotNull(immediateResult);
                    Assert.assertNotNull(immediateResult.getItems());
                    Assert.assertFalse(immediateResult.getItems().contains(targetProduct));
                } else {
                    Product mockProduct = mock(Product.class);
                    when(mockProduct.getId()).thenReturn(targetProductId);
                    productReference.reference = mockProduct;
                    Traveler.addToWishlist(mockProduct, step5WishlistAddCallback);
                }
            }

            @Override
            public void onWishlistFetchError(Error error, int identifier) {
                Log.e("TravelerTest", "fullIntegrationTest::step3WishlistFetchCallback::onWishlistQueryError");
                error.printStackTrace();
                throw error;
            }

            @Nullable
            @Override
            public WishlistResult getPreviousResult() {
                return null;
            }

            @Override
            public void onWishlistFetchReceive(WishlistResult result, int identifier) {
                //no-op
            }
        };

        // 2 Catalog search based on that flight. We will test using the first product of the first catalog group
        CatalogSearchCallback step2CatalogSearchCallback = new CatalogSearchCallback() {
            @Override
            public void onCatalogSuccess(Catalog catalog) {
                Log.v("TravelerTest", "fullIntegrationTest::step2CatalogSearchCallback::onCatalogSuccess");
                Assert.assertTrue(catalog.getGroups().size() >= 1);
                CatalogGroup catalogGroup = catalog.getGroups().get(0);
                Assert.assertTrue(catalogGroup.getItems().size() >= 1);
                Product product = catalogGroup.getItems().get(0);
                Assert.assertNotNull(product);
                productIdReference.reference = product.getId();

                Traveler.fetchWishlist(new WishlistQuery(0, 10, new Date(), new Date()), 0, step3WishlistFetchCallback);
            }

            @Override
            public void onCatalogError(Error error) {
                Log.e("TravelerTest", "fullIntegrationTest::step2CatalogSearchCallback::onCatalogError");
                error.printStackTrace();
                throw error;
            }
        };

        // 1 Flight Search using AC100 at the current day
        FlightSearchCallback step1flightSearchCallback = new FlightSearchCallback() {
            @Override
            public void onFlightSearchSuccess(List<Flight> flights) {
                Log.v("TravelerTest", "fullIntegrationTest::step1flightSearchCallback::onFlightSearchSuccess");
                Assert.assertTrue(flights.size() >= 1);
                CatalogQuery catalogQuery = new CatalogQuery(flights);
                Traveler.fetchCatalog(catalogQuery, step2CatalogSearchCallback);
            }

            @Override
            public void onFlightSearchError(Error error) {
                Log.e("TravelerTest", "fullIntegrationTest::step1flightSearchCallback::onFlightSearchError");
                error.printStackTrace();
                throw error;
            }
        };

        Traveler.flightSearch(query, step1flightSearchCallback);

        expectation.await();
    }

    /**
     * This test can fail if KENSINGTON_2H_TOUR_PRODUCT_ID changes from GLX partners
     */
    @Test
    public void addUnavailableItemToWishlistTest() throws Exception {
        Traveler.identify(TRAVELLER_ID);
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId()).thenReturn(KENSINGTON_2H_TOUR_PRODUCT_ID);

        Expectation expectation = new Expectation();
        Traveler.addToWishlist(product, new WishlistAddCallback() {
            @Override
            public void onWishlistAddSuccess(Product item, CatalogItemDetails itemDetails) {
                Log.v("TravelerTest", "wishlistTestAdd::onWishlistToggleSuccess");
                Assert.assertEquals(KENSINGTON_2H_TOUR_PRODUCT_ID, item.getId());
                Assert.assertEquals(KENSINGTON_2H_TOUR_PRODUCT_ID, itemDetails.getId());
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
}
