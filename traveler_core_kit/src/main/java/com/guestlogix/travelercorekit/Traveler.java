package com.guestlogix.travelercorekit;

import android.content.Context;
import android.text.TextUtils;
import com.guestlogix.travelercorekit.callbacks.*;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.AuthenticatedRequest;
import com.guestlogix.travelercorekit.network.Router;
import com.guestlogix.travelercorekit.task.*;
import com.guestlogix.travelercorekit.utilities.TravelerLog;

import java.util.List;

public class Traveler {
    private static Traveler mLocalInstance;
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
                if (authTokenFetchTask.getError() != null) {
                    throw new RuntimeException(String.format("Could not initialize Traveler SDK. %s ", authTokenFetchTask.getError()));
                } else {
                    mSession.setAuthToken(authTokenFetchTask.getAuthToken());
                }
            }
        };

        BlockTask sessionBeginBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (!TextUtils.isEmpty(sessionBeginTask.getSession().getAuthToken().getValue())) {
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
     * @param flightQuery          Flight query to search flights.
     * @param flightSearchCallback Callback methods which will be executed after the data is fetched.
     */
    public static void flightSearch(FlightQuery flightQuery, FlightSearchCallback flightSearchCallback) {
        if (null == mLocalInstance) {
            flightSearchCallback.onFlightSearchError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedRequest request = Router.searchFlight(mLocalInstance.mSession, flightQuery, mLocalInstance.mSession.getContext());

            AuthenticatedNetworkRequestTask<List<Flight>> searchFlightTask = new AuthenticatedNetworkRequestTask<>(mLocalInstance.mSession, request, new ArrayMappingFactory<>(new Flight.FlightObjectMappingFactory()));

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
            AuthenticatedRequest request = Router.getCatalog(mLocalInstance.mSession, catalogQuery, mLocalInstance.mSession.getContext());

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
     * @param catalogItem                Ids of the flights for which to fetch the groups.
     * @param catalogItemDetailsCallback Callback methods which will be executed after the data is fetched.
     */
    public static void fetchCatalogItemDetails(CatalogItem catalogItem, CatalogItemDetailsCallback catalogItemDetailsCallback) {
        if (null == mLocalInstance) {
            catalogItemDetailsCallback.onCatalogItemDetailsError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedRequest request = Router.getCatalogItem(mLocalInstance.mSession, catalogItem, mLocalInstance.mSession.getContext());
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
     * @param bookingContext            Ids of the flights for which to fetch the groups.
     * @param checkAvailabilityCallback Callback methods which will be executed after the data is fetched.
     */
    public static void checkAvailability(BookingContext bookingContext, CheckAvailabilityCallback checkAvailabilityCallback) {
        if (null == mLocalInstance) {
            checkAvailabilityCallback.onCheckAvailabilityError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {

            if (bookingContext.getSelectedDate() == null) {
                checkAvailabilityCallback.onCheckAvailabilityError(new TravelerError(TravelerErrorCode.NO_DATE, "Booking date must not be null"));
                return;
            }

            bookingContext.setReady(false);

            AuthenticatedRequest request = Router.productSchedule(mLocalInstance.mSession, bookingContext, mLocalInstance.mSession.getContext());
            AuthenticatedNetworkRequestTask<List<Availability>> checkAvailabilityTask = new AuthenticatedNetworkRequestTask<>(mLocalInstance.mSession, request, new ArrayMappingFactory<>(new Availability.AvailabilityObjectMappingFactory()));

            BlockTask searchGroupBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != checkAvailabilityTask.getError()) {
                        checkAvailabilityCallback.onCheckAvailabilityError(checkAvailabilityTask.getError());
                        TravelerLog.e(checkAvailabilityTask.getError().toString());
                    } else {
                        checkAvailabilityCallback.onCheckAvailabilitySuccess(checkAvailabilityTask.getResource());
                    }
                    bookingContext.setReady(true);
                }
            };

            searchGroupBlockTask.addDependency(checkAvailabilityTask);
            mLocalInstance.mTaskManager.addTask(checkAvailabilityTask);
            TaskManager.getMainTaskManager().addTask(searchGroupBlockTask);
        }
    }

    /**
     * Fetches all the passes for a given booking context.
     *
     * @param bookingContext      context for which to fetch.
     * @param fetchPassesCallback Callback methods which will be executed after the data is fetched.
     */
    public static void fetchPass(BookingContext bookingContext, FetchPassesCallback fetchPassesCallback) {
        if (null == mLocalInstance) {
            fetchPassesCallback.onError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            if (bookingContext.getSelectedDate() == null) {
                fetchPassesCallback.onError(new TravelerError(TravelerErrorCode.NO_DATE, "Booking date must not be null"));
                return;
            }

            if (null != bookingContext.getTimeRequired() && bookingContext.getTimeRequired() && bookingContext.getSelectedTime() == null) {
                fetchPassesCallback.onError(new TravelerError(TravelerErrorCode.NO_TIME, "Booking time is required"));
                return;
            }

            AuthenticatedRequest request = Router.productPass(mLocalInstance.mSession, bookingContext, mLocalInstance.mSession.getContext());
            AuthenticatedNetworkRequestTask<List<Pass>> passFetchTask = new AuthenticatedNetworkRequestTask<>(mLocalInstance.mSession, request, new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()));

            BlockTask fetchPassBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != passFetchTask.getError()) {
                        fetchPassesCallback.onError(passFetchTask.getError());
                    } else {
                        fetchPassesCallback.onSuccess(passFetchTask.getResource());
                    }
                }
            };

            fetchPassBlockTask.addDependency(passFetchTask);
            mLocalInstance.mTaskManager.addTask(passFetchTask);
            TaskManager.getMainTaskManager().addTask(fetchPassBlockTask);
        }
    }
}