package com.guestlogix.travelercorekit;

import android.content.Context;
import android.text.TextUtils;
import com.guestlogix.travelercorekit.callbacks.*;
import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.models.TravelerErrorCode;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.tasks.*;
import com.guestlogix.travelercorekit.utilities.*;

import java.util.List;

public class Traveler {
    private static Traveler localInstance;

    private TaskManager taskManager = new TaskManager();
    private Session session;

    public static void initialize(String apiKey, Context applicationContext) {
        if (localInstance != null) {
            TravelerLog.e("SDK already initialized");
        } else {
            localInstance = new Traveler(apiKey, applicationContext);
        }
    }

    private Traveler(String apiKey, Context context) {
        this.session = new Session(apiKey, context);

        //read token from disk
        SessionBeginTask sessionBeginTask = new SessionBeginTask(this.session);
        //fetch token from backend if disk does not have one
        AuthTokenFetchTask authTokenFetchTask = new AuthTokenFetchTask(this.session.getApiKey(), session.getContext());

        BlockTask authTokenFetchBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (authTokenFetchTask.getError() != null) {
                    throw new RuntimeException(String.format("Could not initialize Traveler SDK. %s ", authTokenFetchTask.getError()));
                } else {
                    session.setAuthToken(authTokenFetchTask.getAuthToken());
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

        taskManager.addTask(sessionBeginTask);
        taskManager.addTask(sessionBeginBlockTask);
        taskManager.addTask(authTokenFetchTask);
        taskManager.addTask(authTokenFetchBlockTask);
    }

    /**
     * Fetches groups of catalog items.
     *
     * @param flightQuery          Flight query to search flights.
     * @param flightSearchCallback Callback methods which will be executed after the data is fetched.
     */
    public static void flightSearch(FlightQuery flightQuery, FlightSearchCallback flightSearchCallback) {
        if (null == localInstance) {
            flightSearchCallback.onFlightSearchError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.searchFlight(localInstance.session, flightQuery, localInstance.session.getContext());

            AuthenticatedNetworkRequestTask<List<Flight>> searchFlightTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new Flight.FlightObjectMappingFactory()));

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

            localInstance.taskManager.addTask(searchFlightTask);
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
        if (null == localInstance) {
            catalogSearchCallback.onCatalogSearchError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.getCatalog(localInstance.session, catalogQuery, localInstance.session.getContext());

            AuthenticatedNetworkRequestTask<Catalog> searchGroupTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new Catalog.CatalogObjectMappingFactory());

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
            localInstance.taskManager.addTask(searchGroupTask);
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
        if (null == localInstance) {
            catalogItemDetailsCallback.onCatalogItemDetailsError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.getCatalogItem(localInstance.session, catalogItem, localInstance.session.getContext());
            AuthenticatedNetworkRequestTask<CatalogItemDetails> catalogItemDetailsTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new CatalogItemDetails.CatalogItemDetailsObjectMappingFactory());

            BlockTask catalogItemDetailsBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != catalogItemDetailsTask.getError()) {
                        catalogItemDetailsCallback.onCatalogItemDetailsError(catalogItemDetailsTask.getError());
                    } else {
                        catalogItemDetailsCallback.onCatalogItemDetailsSuccess(catalogItemDetailsTask.getResource());
                    }
                }
            };

            catalogItemDetailsBlockTask.addDependency(catalogItemDetailsTask);
            localInstance.taskManager.addTask(catalogItemDetailsTask);
            TaskManager.getMainTaskManager().addTask(catalogItemDetailsBlockTask);
        }
    }

    /**
     * Fetches groups of catalog items.
     *
     * @param bookingContext            Ids of the flights for which to fetch the groups.
     * @param checkAvailabilityCallback Callback methods which will be executed after the data is fetched.
     */
    public static void checkAvailability(BookingContext bookingContext, CheckAvailabilityCallback checkAvailabilityCallback) {
        if (null == localInstance) {
            checkAvailabilityCallback.onCheckAvailabilityError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {

            if (bookingContext.getSelectedDate() == null) {
                checkAvailabilityCallback.onCheckAvailabilityError(new TravelerError(TravelerErrorCode.NO_DATE, "Booking date must not be null"));
                return;
            }

            bookingContext.setReady(false);

            AuthenticatedUrlRequest request = Router.productSchedule(localInstance.session, bookingContext, localInstance.session.getContext());
            AuthenticatedNetworkRequestTask<List<Availability>> checkAvailabilityTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new Availability.AvailabilityObjectMappingFactory()));

            BlockTask searchGroupBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != checkAvailabilityTask.getError()) {
                        checkAvailabilityCallback.onCheckAvailabilityError(checkAvailabilityTask.getError());
                    } else {
                        checkAvailabilityCallback.onCheckAvailabilitySuccess(checkAvailabilityTask.getResource());
                    }
                    bookingContext.setReady(true);
                }
            };

            searchGroupBlockTask.addDependency(checkAvailabilityTask);
            localInstance.taskManager.addTask(checkAvailabilityTask);
            TaskManager.getMainTaskManager().addTask(searchGroupBlockTask);
        }
    }

    /**
     * Fetches all the passes for a given booking context.
     *
     * @param bookingContext      context for which to fetch.
     * @param fetchPassesCallback Callback methods which will be executed after the data is fetched.
     */
    public static void fetchPasses(BookingContext bookingContext, FetchPassesCallback fetchPassesCallback) {
        if (null == localInstance) {

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

            AuthenticatedUrlRequest request = Router.productPass(localInstance.session, bookingContext, localInstance.session.getContext());
            AuthenticatedNetworkRequestTask<List<Pass>> passFetchTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()));

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
            localInstance.taskManager.addTask(passFetchTask);
            TaskManager.getMainTaskManager().addTask(fetchPassBlockTask);
        }
    }
}