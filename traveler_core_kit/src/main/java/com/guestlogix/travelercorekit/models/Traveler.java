package com.guestlogix.travelercorekit.models;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.AuthenticatedUrlRequest;
import com.guestlogix.travelercorekit.Router;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.*;
import com.guestlogix.travelercorekit.tasks.AuthTokenFetchTask;
import com.guestlogix.travelercorekit.tasks.AuthenticatedNetworkRequestTask;
import com.guestlogix.travelercorekit.tasks.BlockTask;
import com.guestlogix.travelercorekit.tasks.SessionBeginTask;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.TaskManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Traveler {
    private static Traveler localInstance;

    private TaskManager taskManager = new TaskManager();
    private Session session;

    /**
     * Initializes the SDK.
     *
     * @param apiKey             key provided by the Guestlogix platform. Invalid keys will result in forbidden response codes.
     * @param applicationContext application context where the sdk is running.
     */
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
     * Searches all available flights matching the flight query.
     *
     * @param flightQuery          the flight id and the departure date of a flight
     * @param flightSearchCallback callback methods to be executed once the search is complete
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
                        TravelerLog.e(searchFlightTask.getError().getMessage());
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
     * Fetches the catalog for all the flights provided in the catalog query.
     * <p>
     * Must use the long form for flight ids. See {@link Flight#id}.
     *
     * @param catalogQuery          Ids of the flights for which to fetch the groups.
     * @param catalogSearchCallback Callback methods which will be executed after the data is fetched.
     */
    public static void fetchCatalog(CatalogQuery catalogQuery, catalogSearchCallback catalogSearchCallback) {
        if (null == localInstance) {
            catalogSearchCallback.onCatalogError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.catalog(localInstance.session, catalogQuery, localInstance.session.getContext());

            AuthenticatedNetworkRequestTask<Catalog> searchGroupTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new Catalog.CatalogObjectMappingFactory());

            BlockTask searchGroupBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != searchGroupTask.getError()) {
                        catalogSearchCallback.onCatalogError(searchGroupTask.getError());
                        TravelerLog.e(searchGroupTask.getError().getMessage());
                    } else {
                        catalogSearchCallback.onCatalogSuccess(searchGroupTask.getResource());
                    }
                }
            };

            searchGroupBlockTask.addDependency(searchGroupTask);
            localInstance.taskManager.addTask(searchGroupTask);
            TaskManager.getMainTaskManager().addTask(searchGroupBlockTask);
        }
    }

    /**
     * Fetches the details of a catalog item.
     *
     * @param catalogItem                the product for which to fetch details
     * @param catalogItemDetailsCallback callback methods to be executed once the fetch is complete.
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
                        TravelerLog.e(catalogItemDetailsTask.getError().getMessage());
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
     * Fetches availabilities for a product with a given date range. Set the startDate and endDate to the same day
     * for availabilities on a specific day.
     *
     * @param product                   product to fetch availabilities for
     * @param startDate                 start of the date range inclusive
     * @param endDate                   end of date Range inclusive
     * @param checkAvailabilityCallback callback methods to be executed once the availability fetch is complete.
     */
    public static void fetchAvailabilities(Product product, Date startDate, Date endDate, FetchAvailabilitiesCallback checkAvailabilityCallback) {
        if (null == localInstance) {
            checkAvailabilityCallback.onAvailabilityError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.productSchedule(localInstance.session, product, startDate, endDate, localInstance.session.getContext());
            AuthenticatedNetworkRequestTask<List<Availability>> fetchAvailabilitiesTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new Availability.AvailabilityObjectMappingFactory()));

            BlockTask fetchBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != fetchAvailabilitiesTask.getError()) {
                        checkAvailabilityCallback.onAvailabilityError(fetchAvailabilitiesTask.getError());
                        TravelerLog.e(fetchAvailabilitiesTask.getError().getMessage());
                    } else {
                        checkAvailabilityCallback.onAvailabilitySuccess(fetchAvailabilitiesTask.getResource());
                    }
                }
            };

            fetchBlockTask.addDependency(fetchAvailabilitiesTask);
            localInstance.taskManager.addTask(fetchAvailabilitiesTask);
            TaskManager.getMainTaskManager().addTask(fetchBlockTask);
        }
    }

    /**
     * Fetches all passes for a product for a given availability.
     * <p>
     * Use {@link #fetchAvailabilities(Product, Date, Date, FetchAvailabilitiesCallback)} to get a valid availabilities
     * for this product
     *
     * @param product             product to fetch passes for
     * @param availability        availability of the product.
     * @param option              flavour of the availability, leave null if no flavour is needed
     * @param fetchPassesCallback callback methods to be executed once the fetch is complete
     */
    public static void fetchPasses(Product product, Availability availability, @Nullable BookingOption option, FetchPassesCallback fetchPassesCallback) {
        if (null == localInstance) {
            fetchPassesCallback.onPassFetchError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {

            AuthenticatedUrlRequest request = Router.productPass(localInstance.session, product, availability, option, localInstance.session.getContext());
            AuthenticatedNetworkRequestTask<List<Pass>> fetchPassesTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()));

            BlockTask fetchPassBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != fetchPassesTask.getError()) {
                        fetchPassesCallback.onPassFetchError(fetchPassesTask.getError());
                        TravelerLog.e(fetchPassesTask.getError().getMessage());
                    } else {
                        fetchPassesCallback.onPassFetchSuccess(fetchPassesTask.getResource());
                    }
                }
            };

            fetchPassBlockTask.addDependency(fetchPassesTask);
            localInstance.taskManager.addTask(fetchPassesTask);
            TaskManager.getMainTaskManager().addTask(fetchPassBlockTask);
        }
    }

    /**
     * Creates a booking form for a product given a list of passes.
     * <p>
     * Passes <b>can</b> be repeated. If a user selects 2 passes of type 'A' and one pass of type 'B', the list should be
     * of the form: [A, A, B].
     *
     * @param product                  product for which to create a booking form
     * @param passes                   selected passes
     * @param fetchBookingFormCallback callback methods which will be executed after creation is complete.
     */
    public static void fetchBookingForm(Product product, List<Pass> passes, FetchBookingFormCallback fetchBookingFormCallback) {
        if (null == localInstance) {
            fetchBookingFormCallback.onBookingFormFetchError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {

            AuthenticatedUrlRequest request = Router.productQuestion(localInstance.session, product, passes, localInstance.session.getContext());
            AuthenticatedNetworkRequestTask<List<QuestionGroup>> fetchBookingFormTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new QuestionGroup.QuestionGroupObjectMappingFactory()));

            BlockTask fetchBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != fetchBookingFormTask.getError()) {
                        fetchBookingFormCallback.onBookingFormFetchError(fetchBookingFormTask.getError());
                        TravelerLog.e(fetchBookingFormTask.getError().getMessage());
                    } else {
                        fetchBookingFormCallback.onBookingFormFetchSuccess(new BookingForm(product, passes, fetchBookingFormTask.getResource()));
                    }
                }
            };

            fetchBlockTask.addDependency(fetchBookingFormTask);
            localInstance.taskManager.addTask(fetchBookingFormTask);
            TaskManager.getMainTaskManager().addTask(fetchBlockTask);
        }
    }

    /**
     * Creates an order using a booking form.
     *
     * @param bookingForm         A completed booking form
     * @param orderCreateCallback callback methods which to be executed once the order creation is processed.
     */
    public static void createOrder(BookingForm bookingForm, OrderCreateCallback orderCreateCallback) {
        if (null == localInstance) {
            orderCreateCallback.onOrderCreateFailure(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            ArrayList<BookingForm> forms = new ArrayList<>();
            forms.add(bookingForm);
            AuthenticatedUrlRequest request = Router.orderCreate(localInstance.session, forms, localInstance.session.getContext());
            AuthenticatedNetworkRequestTask<Order> createOrderTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new Order.OrderMappingFactory());

            BlockTask fetchBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != createOrderTask.getError()) {
                        orderCreateCallback.onOrderCreateFailure(createOrderTask.getError());
                        TravelerLog.e(createOrderTask.getError().getMessage());
                    } else {
                        orderCreateCallback.onOrderCreateSuccess(createOrderTask.getResource());
                    }
                }
            };

            fetchBlockTask.addDependency(createOrderTask);
            localInstance.taskManager.addTask(createOrderTask);
            TaskManager.getMainTaskManager().addTask(fetchBlockTask);
        }
    }

    /**
     * Processes an order.
     *
     * @param order                order to process
     * @param payment              payment method
     * @param processOrderCallback callback methods to be executed after the order is processed
     */
    public static void processOrder(Order order, Payment payment, ProcessOrderCallback processOrderCallback) {
        if (null == localInstance) {
            processOrderCallback.onOrderProcessError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.orderProcess(localInstance.session, order, payment, localInstance.session.getContext());

            AuthenticatedNetworkRequestTask<Receipt> processOrderTask = new AuthenticatedNetworkRequestTask<>(localInstance.session, request, new Receipt.ReceiptMappingFactory());

            BlockTask fetchBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != processOrderTask.getError()) {
                        processOrderCallback.onOrderProcessError(processOrderTask.getError());
                        TravelerLog.e(processOrderTask.getError().getMessage());
                    } else {
                        processOrderCallback.onOrderProcessSuccess(processOrderTask.getResource());
                    }
                }
            };

            fetchBlockTask.addDependency(processOrderTask);
            localInstance.taskManager.addTask(processOrderTask);
            TaskManager.getMainTaskManager().addTask(fetchBlockTask);

        }
    }
}