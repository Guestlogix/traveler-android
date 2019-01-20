package com.guestlogix.travelercorekit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.callbacks.CheckAvailabilityCallback;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.AuthenticatedRequest;
import com.guestlogix.travelercorekit.network.Router;
import com.guestlogix.travelercorekit.task.*;
import com.guestlogix.travelercorekit.utilities.TravelerLog;

import java.net.URL;
import java.util.ArrayList;

public class Traveler {

    private static Traveler mLocalInstance;
    private static final String TAG = "Traveler";
    private TaskManager mTaskManager = new TaskManager();

    private Session mSession;

    public static void initialize(String apiKey, Context applicationContext) {

        if (mLocalInstance != null) {
            TravelerLog.e("SDK already initialized");
        } else {
            mLocalInstance = new Traveler(apiKey, applicationContext);
        }
    }

    private Traveler(String apiKey, Context context) {
        this.mSession = new Session(apiKey, context);

        //read token from disk
        SessionBeginTask sessionBeginTask = new SessionBeginTask(this.mSession);
        //fetch token from backend if disk does not have one
        AuthTokenFetchTask authTokenFetchTask = new AuthTokenFetchTask(this.mSession.getApiKey(), mSession.getContext());

        BlockTask authTokenFetchBlockTask = new BlockTask() {
            @Override
            protected void main() {
                //Log.v("Traveler", "Setting Session token:" + authTokenFetchTask.getAuthToken().getValue());

                mSession.setAuthToken(authTokenFetchTask.getAuthToken());
            }
        };

        BlockTask sessionBeginBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (TextUtils.isEmpty(sessionBeginTask.getSession().getAuthToken().getValue())) {
                    Log.v("Traveler", "Could not Find Token on disk, continue fetch token task...");
                } else {
                    Log.v("Traveler", "Found Token on disk: " + sessionBeginTask.getSession().getAuthToken().getValue());
                    Log.v("Traveler", "Cancelling fetch token tasks");

                    authTokenFetchTask.cancel();
                    authTokenFetchBlockTask.cancel();
                }
            }
        };

        sessionBeginBlockTask.addDependency(sessionBeginTask);
        authTokenFetchTask.addDependency(sessionBeginBlockTask);
        authTokenFetchBlockTask.addDependency(authTokenFetchTask);

        mTaskManager.addTask(sessionBeginTask);
        mTaskManager.addTask(sessionBeginBlockTask);
        mTaskManager.addTask(authTokenFetchTask);
        mTaskManager.addTask(authTokenFetchBlockTask);

    }

    /**
     * Fetches groups of catalog items.
     *
     * @param imageUrl URL of image to download.
     * @param imageView ImageView to load the image in.
     */
    public static void loadImage(URL imageUrl, ImageView imageView) {

        if (null == mLocalInstance) {
            throw new RuntimeException("SDK not initialized, Initialize by calling Traveler.initialize();");
        } else {

            DownloadImageTask downloadImageTask = new DownloadImageTask(imageUrl);

            BlockTask searchFlightBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != downloadImageTask.getResource()) {
                        imageView.setImageBitmap(downloadImageTask.getResource());
                    } else {
                        //TODO set placeholder
                    }
                }
            };

            searchFlightBlockTask.addDependency(downloadImageTask);

            mLocalInstance.mTaskManager.addTask(downloadImageTask);
            TaskManager.getMainTaskManager().addTask(searchFlightBlockTask);

        }
    }

    /**
     * Fetches groups of catalog items.
     *
     * @param flightQuery          Flight query to search flights.
     * @param flightSearchCallback Callback methods which will be executed after the data is fetched.
     */
    public static void flightSearch(FlightQuery flightQuery, FlightSearchCallback flightSearchCallback) {

        if (null == mLocalInstance) {
            flightSearchCallback.onFlightSearchError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedRequest request = Router.searchFlight(mLocalInstance.mSession, flightQuery);

            AuthenticatedNetworkRequestTask<ArrayList<Flight>> searchFlightTask = new AuthenticatedNetworkRequestTask<>(mLocalInstance.mSession, request, new ArrayMappingFactory<>(new Flight.FlightObjectMappingFactory()));


            BlockTask searchFlightBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != searchFlightTask.getError()) {
                        flightSearchCallback.onFlightSearchError(searchFlightTask.getError());
                    } else {
                        flightSearchCallback.onFlightSearchSuccess(searchFlightTask.getResource());
                    }
                }
            };

            searchFlightBlockTask.addDependency(searchFlightTask);

            mLocalInstance.mTaskManager.addTask(searchFlightTask);
            TaskManager.getMainTaskManager().addTask(searchFlightBlockTask);

        }
    }

    /**
     * Fetches groups of catalog items.
     *
     * @param catalogQuery          Ids of the flights for which to fetch the groups.
     * @param catalogSearchCallback Callback methods which will be executed after the data is fetched.
     */
    public static void fetchCatalog(CatalogQuery catalogQuery, CatalogSearchCallback catalogSearchCallback) {
        if (null == mLocalInstance) {
            catalogSearchCallback.onCatalogSearchError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedRequest request = Router.getCatalog(mLocalInstance.mSession, catalogQuery);

            AuthenticatedNetworkRequestTask<Catalog> searchGroupTask = new AuthenticatedNetworkRequestTask<>(mLocalInstance.mSession, request, new Catalog.CatalogObjectMappingFactory());

            BlockTask searchGroupBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != searchGroupTask.getError()) {
                        catalogSearchCallback.onCatalogSearchError(searchGroupTask.getError());
                    } else {
                        catalogSearchCallback.onCatalogSearchSuccess(searchGroupTask.getResource());
                    }
                }
            };

            searchGroupBlockTask.addDependency(searchGroupTask);
            mLocalInstance.mTaskManager.addTask(searchGroupTask);
            TaskManager.getMainTaskManager().addTask(searchGroupBlockTask);
        }
    }

    /**
     * Fetches groups of catalog items.
     *
     * @param catalogItem          Ids of the flights for which to fetch the groups.
     * @param catalogItemDetailsCallback Callback methods which will be executed after the data is fetched.
     */
    public static void fetchCatalogItemDetails(CatalogItem catalogItem, CatalogItemDetailsCallback catalogItemDetailsCallback) {
        if (null == mLocalInstance) {
            catalogItemDetailsCallback.onCatalogItemDetailsError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedRequest request = Router.getCatalogItem(mLocalInstance.mSession, catalogItem);

            AuthenticatedNetworkRequestTask<CatalogItemDetails> catalogItemDetailsTask = new AuthenticatedNetworkRequestTask<>(mLocalInstance.mSession, request, new CatalogItemDetails.CatalogItemDetailsObjectMappingFactory());

            BlockTask searchGroupBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != catalogItemDetailsTask.getError()) {
                        catalogItemDetailsCallback.onCatalogItemDetailsError(catalogItemDetailsTask.getError());
                    } else {
                        catalogItemDetailsCallback.onCatalogItemDetailsSuccess(catalogItemDetailsTask.getResource());
                    }
                }
            };

            searchGroupBlockTask.addDependency(catalogItemDetailsTask);
            mLocalInstance.mTaskManager.addTask(catalogItemDetailsTask);
            TaskManager.getMainTaskManager().addTask(searchGroupBlockTask);
        }
    }

    /**
     * Fetches groups of catalog items.
     *
     * @param bookingContext          Ids of the flights for which to fetch the groups.
     * @param checkAvailabilityCallback Callback methods which will be executed after the data is fetched.
     */
    public static void checkAvailability(BookingContext bookingContext, CheckAvailabilityCallback checkAvailabilityCallback) {
        if (null == mLocalInstance) {
            checkAvailabilityCallback.onCheckAvailabilityError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedRequest request = Router.productSchedule(mLocalInstance.mSession, bookingContext);

            AuthenticatedNetworkRequestTask<ArrayList<Availability>> checkAvailabilityTask = new AuthenticatedNetworkRequestTask<>(mLocalInstance.mSession, request,  new ArrayMappingFactory<>(new Availability.AvailabilityObjectMappingFactory()));

            BlockTask searchGroupBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != checkAvailabilityTask.getError()) {
                        checkAvailabilityCallback.onCheckAvailabilityError(checkAvailabilityTask.getError());
                    } else {
                        checkAvailabilityCallback.onCheckAvailabilitySuccess(checkAvailabilityTask.getResource());
                    }
                }
            };

            searchGroupBlockTask.addDependency(checkAvailabilityTask);
            mLocalInstance.mTaskManager.addTask(checkAvailabilityTask);
            TaskManager.getMainTaskManager().addTask(searchGroupBlockTask);
        }
    }

}