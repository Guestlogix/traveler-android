package com.guestlogix.travelercorekit.models;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.AuthenticatedUrlRequest;
import com.guestlogix.travelercorekit.Router;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.*;
import com.guestlogix.travelercorekit.tasks.*;
import com.guestlogix.travelercorekit.utilities.*;

import java.util.ArrayList;
import java.util.Date;
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
            AuthenticatedUrlRequest request = Router.catalog(localInstance.session, catalogQuery, localInstance.session.getContext());

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
            AuthenticatedUrlRequest request = Router.product(localInstance.session, catalogItem, localInstance.session.getContext());
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
     * Fetches all availabilities in the given date range.
     *
     * @param product                   Product to fetch availabilities for.
     * @param startDate                 Start of the date range inclusive.
     * @param endDate                   End of date Range inclusive.
     * @param checkAvailabilityCallback Methods that will be executed after the availabilities are fetched.
     */
    public static void fetchAvailabilities(Product product, Date startDate, Date endDate, FetchAvailabilitiesCallback checkAvailabilityCallback) {
        if (null == localInstance) {
            checkAvailabilityCallback.onAvailabilityError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.productSchedule(localInstance.session, product, startDate, endDate, localInstance.session.getContext());
            AuthenticatedNetworkRequestTask<List<Availability>> fetchTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new Availability.AvailabilityObjectMappingFactory()));

            BlockTask fetchBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != fetchTask.getError()) {
                        checkAvailabilityCallback.onAvailabilityError(fetchTask.getError());
                    } else {
                        checkAvailabilityCallback.onAvailabilitySuccess(fetchTask.getResource());
                    }
                }
            };

            fetchBlockTask.addDependency(fetchTask);
            localInstance.taskManager.addTask(fetchTask);
            TaskManager.getMainTaskManager().addTask(fetchBlockTask);
        }
    }

    /**
     * Fetches passes for a product given an availability and an optional booking option.
     *
     * @param product             Product to fetch passes for.
     * @param availability        Specific availability in the product
     * @param option              Optional BookingOption
     * @param fetchPassesCallback callback methods which will be executed after the data is fetched.
     */
    public static void fetchPasses(Product product, Availability availability, @Nullable BookingOption option, FetchPassesCallback fetchPassesCallback) {
        if (null == localInstance) {
            fetchPassesCallback.onError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {

            AuthenticatedUrlRequest request = Router.productPass(localInstance.session, product, availability, option, localInstance.session.getContext());
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

    public static void fetchBookingForm(Product product, List<Pass> passes, FetchBookingFormCallback fetchBookingFormCallback) {
        if (null == localInstance) {
            fetchBookingFormCallback.onBookingFormFetchError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {

            AuthenticatedUrlRequest request = Router.productQuestion(localInstance.session, product, passes, localInstance.session.getContext());
            AuthenticatedNetworkRequestTask<List<QuestionGroup>> fetchTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new QuestionGroup.QuestionGroupObjectMappingFactory()));

            BlockTask fetchBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != fetchTask.getError()) {
                        fetchBookingFormCallback.onBookingFormFetchError(fetchTask.getError());
                    } else {
                        fetchBookingFormCallback.onBookingFormFetchSuccess(new BookingForm(product, passes, fetchTask.getResource()));
                    }
                }
            };

            fetchBlockTask.addDependency(fetchTask);
            localInstance.taskManager.addTask(fetchTask);
            TaskManager.getMainTaskManager().addTask(fetchBlockTask);
        }
    }

    public static void createOrder(BookingForm bookingForm, OrderCreateCallback orderCreateCallback) {
        if (null == localInstance) {
            orderCreateCallback.onOrderCreateFailure(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            ArrayList<BookingForm> forms = new ArrayList<>();
            forms.add(bookingForm);
            AuthenticatedUrlRequest request = Router.orderCreate(localInstance.session, forms, localInstance.session.getContext());

            // TODO;
        }
    }
}