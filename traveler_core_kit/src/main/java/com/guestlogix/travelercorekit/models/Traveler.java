package com.guestlogix.travelercorekit.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.AuthenticatedUrlRequest;
import com.guestlogix.travelercorekit.Router;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.*;
import com.guestlogix.travelercorekit.tasks.AuthTokenFetchTask;
import com.guestlogix.travelercorekit.tasks.AuthenticatedRemoteNetworkRequestTask;
import com.guestlogix.travelercorekit.tasks.BlockTask;
import com.guestlogix.travelercorekit.tasks.SessionBeginTask;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.ExistsOnlyForTesting;
import com.guestlogix.travelercorekit.utilities.Task;
import com.guestlogix.travelercorekit.utilities.TaskManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Traveler {
    private static final String TAG = "Traveler";
    private static Traveler localInstance;

    private TaskManager taskManager;
    private TaskManager orderSerialTaskManager;
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
            localInstance = new Traveler(apiKey, applicationContext,
                    new TaskManager(),
                    new TaskManager(TaskManager.Mode.SERIAL));
        }
    }

    @ExistsOnlyForTesting
    public static Traveler initializeForTesting(String apiKey,
                                                Context applicationContext,
                                                Session session) {
        localInstance = new Traveler(apiKey, applicationContext,
                new TaskManager(),
                new TaskManager(TaskManager.Mode.SERIAL));
        localInstance.session = session;
        return localInstance;
    }

    private Traveler(String apiKey, Context context, TaskManager taskManager,
                     TaskManager orderSerialTaskManager) {
        this.taskManager = taskManager;
        this.orderSerialTaskManager = orderSerialTaskManager;
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
                    session.setToken(authTokenFetchTask.getAuthToken());
                }
            }
        };

        BlockTask sessionBeginBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (!TextUtils.isEmpty(sessionBeginTask.getSession().getToken().getValue())) {
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

    public static void identify(String identifier) {
        if (null == localInstance) {
            Log.e(TAG, "SDK Not initialized.");
            return;
        }

        localInstance.session.setIdentity(identifier);
    }

    /**
     * Searches all available flights matching the flight query.
     *
     * @param flightQuery          the flight id and the departure date of a flight
     * @param flightSearchCallback callback methods to be executed once the search is complete
     */
    public static void flightSearch(FlightQuery flightQuery, FlightSearchCallback flightSearchCallback) {
        if (null == localInstance) {
            flightSearchCallback.onFlightSearchError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED,
                    "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.searchFlight(localInstance.session, flightQuery,
                    localInstance.session.getContext());

            AuthenticatedRemoteNetworkRequestTask<List<Flight>> searchFlightTask =
                    new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request,
                            new ArrayMappingFactory<>(new Flight.FlightObjectMappingFactory()));

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
     * Must use the long form for flight ids.
     *
     * @param catalogQuery          Ids of the flights for which to fetch the groups.
     * @param catalogSearchCallback Callback methods which will be executed after the data is fetched.
     */
    public static void fetchCatalog(CatalogQuery catalogQuery, CatalogSearchCallback catalogSearchCallback) {
        if (null == localInstance) {
            catalogSearchCallback.onCatalogError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.catalog(localInstance.session, catalogQuery, localInstance.session.getContext());

            AuthenticatedRemoteNetworkRequestTask<Catalog> searchGroupTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new Catalog.CatalogObjectMappingFactory());

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
    public static void fetchCatalogItemDetails(Product catalogItem, CatalogItemDetailsCallback catalogItemDetailsCallback) {
        if (null == localInstance) {
            catalogItemDetailsCallback.onCatalogItemDetailsError(new TravelerError(TravelerErrorCode.SDK_NOT_INITIALIZED, "SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedUrlRequest request = Router.product(localInstance.session, catalogItem, localInstance.session.getContext());
            AuthenticatedRemoteNetworkRequestTask<CatalogItemDetails> catalogItemDetailsTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new CatalogItemDetails.CatalogItemDetailsObjectMappingFactory());

            BlockTask catalogItemDetailsBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != catalogItemDetailsTask.getError()) {
                        catalogItemDetailsCallback.onCatalogItemDetailsError(catalogItemDetailsTask.getError());
                        // TODO: Stop using TravelerLog completely
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
            AuthenticatedRemoteNetworkRequestTask<List<Availability>> fetchAvailabilitiesTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new Availability.AvailabilityObjectMappingFactory()));

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
            AuthenticatedRemoteNetworkRequestTask<List<Pass>> fetchPassesTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()));

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
            AuthenticatedRemoteNetworkRequestTask<List<QuestionGroup>> fetchBookingFormTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new ArrayMappingFactory<>(new QuestionGroup.QuestionGroupObjectMappingFactory()));

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
            AuthenticatedRemoteNetworkRequestTask<Order> createOrderTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new Order.OrderMappingFactory());

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

            AuthenticatedRemoteNetworkRequestTask<Order> processOrderTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new Order.OrderMappingFactory());

            BlockTask fetchBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (null != processOrderTask.getError()) {
                        processOrderCallback.onOrderProcessError(processOrderTask.getError());
                        TravelerLog.e(processOrderTask.getError().getMessage());
                    } else {
                        Receipt receipt = new Receipt(processOrderTask.getResource(), payment);
                        processOrderCallback.onOrderProcessSuccess(receipt);
                    }
                }
            };

            fetchBlockTask.addDependency(processOrderTask);
            localInstance.taskManager.addTask(processOrderTask);
            TaskManager.getMainTaskManager().addTask(fetchBlockTask);

        }
    }

    /**
     * Fetches an `OrderResult` corresponding to the given `OrderQuery`.
     *
     * @param query               The `OrderQuery` to filter
     * @param identifier          An optional `int` identifying the request. This value is returned back in the callbacks. Use this to distinguish between different requests
     * @param callback            Callback methods to be executed once the results are ready
     */

    public static void fetchOrders(OrderQuery query, int identifier, FetchOrdersCallback callback) {
        // TODO: Better check and logging for localInstances
        if (localInstance == null) {
            return;
        }

        if (localInstance.session.getIdentity() == null) {
            callback.onOrdersFetchError(new OrderResultError(OrderResultError.Code.UNIDENTIFIED_TRAVELER), identifier);
            return;
        }

        // TODO: Investigate if context needs to be part of Session or just localInstance
        AuthenticatedUrlRequest request = Router.orders(query, localInstance.session, localInstance.session.getContext());
        AuthenticatedRemoteNetworkRequestTask<OrderResult> fetchTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new OrderResult.OrderResultMappingFactory());

        class ResultWrapper {
            OrderResult result;
        }

        final ResultWrapper resultWrapper = new ResultWrapper();

        BlockTask mergeTask = new BlockTask() {
            @Override
            protected void main() {
                if (fetchTask.getResource() == null) {
                    return;
                }

                OrderResult previousResult = callback.getPreviousResult(identifier);
                if (previousResult != null) {
                    resultWrapper.result = previousResult.merge(fetchTask.getResource());
                }

                if (resultWrapper.result == null)
                    resultWrapper.result = fetchTask.getResource();

                callback.onOrdersFetchReceive(resultWrapper.result, identifier);
            }
        };

        BlockTask blockTask = new BlockTask() {
            @Override
            protected void main() {
                if (fetchTask.getError() != null) {
                    callback.onOrdersFetchError(fetchTask.getError(), identifier);
                    // TODO: There is a log on all error callbacks, remove all of them
                } else {
                    // TODO: rename all local variable to that of a shorter one, similar to this
                    callback.onOrdersFetchSuccess(resultWrapper.result, identifier);
                }
            }
        };

        mergeTask.addDependency(fetchTask);
        blockTask.addDependency(mergeTask);

        localInstance.taskManager.addTask(fetchTask);
        localInstance.orderSerialTaskManager.addTask(mergeTask);
        TaskManager.getMainTaskManager().addTask(blockTask);
    }

    public static void fetchCancellationQuote(Order order, CancellationQuoteCallback callback) {
        // TODO: Better check and logging for localInstances
        if (localInstance == null) {
            return;
        }

        AuthenticatedUrlRequest request = Router.cancellationQuote(order, localInstance.session, localInstance.session.getContext());
        AuthenticatedRemoteNetworkRequestTask<CancellationQuote.Response> fetchTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new CancellationQuote.Response.ResponseObjectMappingFactory());
        BlockTask blockTask = new BlockTask() {
            @Override
            protected void main() {
                if (fetchTask.getError() != null) {
                    callback.onCancellationQuoteError(fetchTask.getError());
                } else {
                    CancellationQuote quote = new CancellationQuote(fetchTask.getResource(), order);
                    callback.onCancellationQuoteSuccess(quote);
                }
            }
        };

        blockTask.addDependency(fetchTask);

        localInstance.taskManager.addTask(fetchTask);
        TaskManager.getMainTaskManager().addTask(blockTask);
    }

    public static void cancelOrder(CancellationQuote quote, CancellationCallback callback) {
        // TODO: Better check and logging for localInstances
        if (localInstance == null) {
            return;
        }

        AuthenticatedUrlRequest request = Router.cancelOrder(quote, localInstance.session, localInstance.session.getContext());
        AuthenticatedRemoteNetworkRequestTask<Order> fetchTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, new Order.OrderMappingFactory());
        BlockTask blockTask = new BlockTask() {
            @Override
            protected void main() {
                if (fetchTask.getError() != null) {
                    callback.onCancellationError(fetchTask.getError());
                } else {
                    callback.onCancellationSuccess(fetchTask.getResource());
                }
            }
        };

        blockTask.addDependency(fetchTask);

        localInstance.taskManager.addTask(fetchTask);
        TaskManager.getMainTaskManager().addTask(blockTask);
    }

    public static void emailOrderConfirmation(Order order, EmailOrderConfirmationCallback callback) {
        // TODO: Better check and logging for localInstances
        if (localInstance == null) {
            return;
        }

        AuthenticatedUrlRequest request = Router.emailOrderConfirmation(order, localInstance.session, localInstance.session.getContext());
        AuthenticatedRemoteNetworkRequestTask requestTask = new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, request, null);
        BlockTask blockTask = new BlockTask() {
            @Override
            protected void main() {
                if (requestTask.getError() != null) {
                    callback.onEmailError(requestTask.getError());
                } else {
                    callback.onEmailSuccess();
                }
            }
        };

        blockTask.addDependency(requestTask);

        localInstance.taskManager.addTask(requestTask);
        TaskManager.getMainTaskManager().addTask(blockTask);
    }
}