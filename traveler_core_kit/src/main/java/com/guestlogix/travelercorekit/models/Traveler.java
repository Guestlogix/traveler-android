package com.guestlogix.travelercorekit.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.AuthenticatedUrlRequest;
import com.guestlogix.travelercorekit.Router;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.BookingItemDetailsCallback;
import com.guestlogix.travelercorekit.callbacks.BookingSearchCallback;
import com.guestlogix.travelercorekit.callbacks.CancellationCallback;
import com.guestlogix.travelercorekit.callbacks.CancellationQuoteCallback;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.callbacks.EmailOrderConfirmationCallback;
import com.guestlogix.travelercorekit.callbacks.FetchAvailabilitiesCallback;
import com.guestlogix.travelercorekit.callbacks.FetchPurchaseFormCallback;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.callbacks.OrderCreateCallback;
import com.guestlogix.travelercorekit.callbacks.ParkingSearchCallback;
import com.guestlogix.travelercorekit.callbacks.ProcessOrderCallback;
import com.guestlogix.travelercorekit.callbacks.WishlistAddCallback;
import com.guestlogix.travelercorekit.callbacks.WishlistFetchCallback;
import com.guestlogix.travelercorekit.callbacks.WishlistRemoveCallback;
import com.guestlogix.travelercorekit.tasks.AuthTokenFetchTask;
import com.guestlogix.travelercorekit.tasks.AuthenticatedRemoteNetworkRequestTask;
import com.guestlogix.travelercorekit.tasks.BlockTask;
import com.guestlogix.travelercorekit.tasks.SessionBeginTask;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.TaskManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

public class Traveler {
    private static String TAG = "Traveler";
    private static Traveler localInstance;

    private TaskManager taskManager = new TaskManager();
    private TaskManager orderSerialTaskManager = new TaskManager(TaskManager.Mode.SERIAL);
    private Session session;
    private Context applicationContext;

    /**
     * Initializes the SDK.
     *
     * @param apiKey             key provided by the Guestlogix platform. Invalid keys will result in forbidden response codes.
     * @param applicationContext application context where the sdk is running.
     */
    public static void initialize(String apiKey, Context applicationContext, boolean isSandboxMode) {
        if (localInstance != null) {
            TravelerLog.e("SDK already initialized");
        } else if (TextUtils.isEmpty(apiKey) || apiKey.equalsIgnoreCase("null")) {
            TravelerLog.e("SDK cannot be initialized with an empty API key!");
        } else {
            localInstance = new Traveler(apiKey, applicationContext, isSandboxMode);
        }
    }

    public static void initialize(String apiKey, Context applicationContext) {
        initialize(apiKey, applicationContext, false);
    }

    private Traveler(String apiKey, Context applicationContext, boolean isSandboxMode) {
        this.session = new Session(apiKey);
        this.applicationContext = applicationContext;
        Router.setSandBoxMode(isSandboxMode);

        //read token from disk
        SessionBeginTask sessionBeginTask = new SessionBeginTask(this.session, applicationContext);
        //fetch token from backend if disk does not have one
        AuthTokenFetchTask authTokenFetchTask = new AuthTokenFetchTask(this.session.getApiKey(), applicationContext);

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

    private static boolean isInitialized() {
        if (null == localInstance) {
            Log.e(TAG, "SDK not initialized, Initialize by calling Traveler.initialize();");
            return false;
        }
        return true;
    }

    public static void identify(String identifier) {
        if (!isInitialized()) return;
        localInstance.session.setIdentity(identifier);
    }

    /**
     * Searches all available flights matching the flight query.
     *
     * @param flightQuery          the flight id and the departure date of a flight
     * @param flightSearchCallback callback methods to be executed once the search is complete
     */
    public static void flightSearch(FlightQuery flightQuery, FlightSearchCallback flightSearchCallback) {
        if (!isInitialized()) return;

        AuthenticatedUrlRequest request = Router.searchFlight(localInstance.session, flightQuery, localInstance.applicationContext);

        AuthenticatedRemoteNetworkRequestTask<List<Flight>> searchFlightTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext, request,
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

    /**
     * Fetches the catalog for all the flights provided in the catalog query.
     * <p>
     * Must use the long form for flight ids.
     *
     * @param catalogQuery          Ids of the flights for which to fetch the groups.
     * @param catalogSearchCallback Callback methods which will be executed after the data is fetched.
     */
    public static void fetchCatalog(CatalogQuery catalogQuery, CatalogSearchCallback catalogSearchCallback) {
        if (!isInitialized()) return;

        AuthenticatedUrlRequest request = Router.catalog(localInstance.session, catalogQuery, localInstance.applicationContext);

        AuthenticatedRemoteNetworkRequestTask<Catalog> searchGroupTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext,
                        request, new Catalog.CatalogObjectMappingFactory());

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

    /**
     * Fetches bookings bases on the provided search criteria
     *
     * @param bookingItemQuery      Search query for which to fetch the bookings
     * @param bookingSearchCallback Callback methods which will be executed after the data is fetched.
     */
    public static void searchBookingItems(BookingItemQuery bookingItemQuery, BookingSearchCallback bookingSearchCallback) {
        if (!isInitialized()) return;

        AuthenticatedUrlRequest request = Router.searchBookingItems(localInstance.session, bookingItemQuery, localInstance.applicationContext);

        AuthenticatedRemoteNetworkRequestTask<BookingItemSearchResult> searchBookingTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext,
                        request, new BookingItemSearchResult.BookingItemSearchResultObjectMappingFactory());

        BlockTask searchGroupBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != searchBookingTask.getError()) {
                    bookingSearchCallback.onBookingSearchError(searchBookingTask.getError());
                    TravelerLog.e(searchBookingTask.getError().getMessage());
                } else {
                    bookingSearchCallback.onBookingSearchSuccess(searchBookingTask.getResource());
                }
            }
        };

        searchGroupBlockTask.addDependency(searchBookingTask);
        localInstance.taskManager.addTask(searchBookingTask);
        TaskManager.getMainTaskManager().addTask(searchGroupBlockTask);
    }

    /**
     * Fetches bookings bases on the provided search criteria
     *
     * @param parkingItemQuery      Search query for which to fetch the bookings
     * @param parkingSearchCallback Callback methods which will be executed after the data is fetched.
     */
    public static void searchParkingItems(ParkingItemQuery parkingItemQuery, ParkingSearchCallback parkingSearchCallback) {
        if (!isInitialized()) return;

        AuthenticatedUrlRequest request = Router.searchParkingItems(localInstance.session, parkingItemQuery, localInstance.applicationContext);

        AuthenticatedRemoteNetworkRequestTask<ParkingItemSearchResult> searchBookingTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext,
                        request, new ParkingItemSearchResult.ParkingItemSearchResultObjectMappingFactory());

        BlockTask searchGroupBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != searchBookingTask.getError()) {
                    parkingSearchCallback.onParkingSearchError(searchBookingTask.getError());
                    TravelerLog.e(searchBookingTask.getError().getMessage());
                } else {
                    parkingSearchCallback.onParkingSearchSuccess(searchBookingTask.getResource());
                }
            }
        };

        searchGroupBlockTask.addDependency(searchBookingTask);
        localInstance.taskManager.addTask(searchBookingTask);
        TaskManager.getMainTaskManager().addTask(searchGroupBlockTask);
    }

    /**
     * Fetches the details of a catalog item.
     *
     * @param product                the product for which to fetch details
     * @param productDetailsCallback callback methods to be executed once the fetch is complete.
     */
    public static void fetchProductDetails(Product product, BookingItemDetailsCallback productDetailsCallback) {
        if (!isInitialized()) return;

        AuthenticatedUrlRequest request;
        AuthenticatedRemoteNetworkRequestTask<CatalogItemDetails> catalogItemDetailsTask = null;

        switch (product.getProductType()) {
            case BOOKABLE:
                request = Router.bookingItem(localInstance.session, product, localInstance.applicationContext);
                catalogItemDetailsTask = new AuthenticatedRemoteNetworkRequestTask<>(
                        localInstance.session,
                        localInstance.applicationContext,
                        request,
                        new BookingItemDetails.BookingItemDetailsObjectMappingFactory());
                break;
            case PARKING:
                request = Router.parkingItem(localInstance.session, product, localInstance.applicationContext);
                catalogItemDetailsTask = new AuthenticatedRemoteNetworkRequestTask<>(
                        localInstance.session,
                        localInstance.applicationContext,
                        request,
                        new ParkingItemDetails.ParkingItemDetailsObjectMappingFactory());
                break;
        }

        AuthenticatedRemoteNetworkRequestTask<CatalogItemDetails> finalCatalogItemDetailsTask = catalogItemDetailsTask;
        BlockTask bookingItemDetailsBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != finalCatalogItemDetailsTask.getError()) {
                    productDetailsCallback.onBookingItemDetailsError(finalCatalogItemDetailsTask.getError());
                    // TODO: Stop using TravelerLog completely
                    TravelerLog.e(finalCatalogItemDetailsTask.getError().getMessage());
                } else {
                    productDetailsCallback.onBookingItemDetailsSuccess(finalCatalogItemDetailsTask.getResource());
                }
            }
        };

        bookingItemDetailsBlockTask.addDependency(catalogItemDetailsTask);
        localInstance.taskManager.addTask(catalogItemDetailsTask);
        TaskManager.getMainTaskManager().addTask(bookingItemDetailsBlockTask);
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
        if (!isInitialized()) return;

        if (endDate.before(startDate)) {
            Log.e(TAG, "endDate should be after startDate");
            return;
        }

        if (endDate.before(new Date())) {
            checkAvailabilityCallback.onAvailabilitySuccess(new ArrayList<>());
            return;
        }

        Date date = startDate;
        if (startDate.before(new Date())) {
            date = new Date();
        }

        AuthenticatedUrlRequest request = Router.productSchedule(localInstance.session, product, date, endDate, localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask<List<Availability>> fetchAvailabilitiesTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext, request,
                        new ArrayMappingFactory<>(new Availability.AvailabilityObjectMappingFactory()));
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
        if (!isInitialized()) return;

        AuthenticatedUrlRequest request = Router.productPass(localInstance.session, product, availability, option, localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask<List<Pass>> fetchPassesTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext, request,
                        new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()));

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

    /**
     * Creates a booking form for a product given a list of passes.
     * <p>
     * Passes <b>can</b> be repeated. If a user selects 2 passes of type 'A' and one pass of type 'B', the list should be
     * of the form: [A, A, B].
     *
     * @param product                  product for which to create a booking form
     * @param passes                   selected passes
     * @param fetchPurchaseFormCallback callback methods which will be executed after creation is complete.
     */
    public static void fetchPurchaseForm(Product product, List<Pass> passes, FetchPurchaseFormCallback fetchPurchaseFormCallback) {
        if (!isInitialized()) return;

        if (passes.size() == 0) {
            fetchPurchaseFormCallback.onPurchaseFormFetchError(new PurchaseError(PurchaseError.Code.NO_PASSES));
            return;
        }

        AuthenticatedUrlRequest request;
        switch (product.getProductType()) {
            case BOOKABLE:
                request = Router.bookingQuestions(localInstance.session, product, passes, localInstance.applicationContext);
                break;
            case PARKING:
                request = Router.parkingQuestions(localInstance.session, product, localInstance.applicationContext);
                break;
            default:
                Log.e(TAG, "fetchPurchaseForm called with product of unknown product type:" + product.getProductType());
                return;
        }
        AuthenticatedRemoteNetworkRequestTask<List<QuestionGroup>> fetchPurchaseFormTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext,
                        request, new ArrayMappingFactory<>(new QuestionGroup.QuestionGroupObjectMappingFactory()));

        BlockTask fetchBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != fetchPurchaseFormTask.getError()) {
                    fetchPurchaseFormCallback.onPurchaseFormFetchError(fetchPurchaseFormTask.getError());
                    TravelerLog.e(fetchPurchaseFormTask.getError().getMessage());
                } else {
                    fetchPurchaseFormCallback.onPurchaseFormFetchSuccess(new PurchaseForm(product, passes, fetchPurchaseFormTask.getResource()));
                }
            }
        };

        fetchBlockTask.addDependency(fetchPurchaseFormTask);
        localInstance.taskManager.addTask(fetchPurchaseFormTask);
        TaskManager.getMainTaskManager().addTask(fetchBlockTask);
    }

    /**
     * Creates an order using a booking form.
     *
     * @param purchaseForm         A completed purchase form
     * @param orderCreateCallback callback methods which to be executed once the order creation is processed.
     */
    public static void createOrder(PurchaseForm purchaseForm, OrderCreateCallback orderCreateCallback) {
        if (!isInitialized()) return;

        ArrayList<PurchaseForm> forms = new ArrayList<>();
        forms.add(purchaseForm);
        AuthenticatedUrlRequest request = Router.createOrder(localInstance.session, forms, localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask<Order> createOrderTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext,
                        request, new Order.OrderMappingFactory());

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

    /**
     * Processes an order.
     *
     * @param order                order to process
     * @param payment              payment method
     * @param processOrderCallback callback methods to be executed after the order is processed
     */
    public static void processOrder(Order order, Payment payment, ProcessOrderCallback processOrderCallback) {
        if (!isInitialized()) return;
        AuthenticatedUrlRequest request = Router.orderProcess(localInstance.session, order, payment, localInstance.applicationContext);

        AuthenticatedRemoteNetworkRequestTask<Order> processOrderTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext,
                        request, new Order.OrderMappingFactory());

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

    /**
     * Fetches an `OrderResult` corresponding to the given `OrderQuery`.
     *
     * @param query               The `OrderQuery` to filter
     * @param identifier          An optional `int` identifying the request. This value is returned back in the callbacks. Use this to distinguish between different requests
     * @param callback            Callback methods to be executed once the results are ready
     */

    public static void fetchOrders(OrderQuery query, int identifier, FetchOrdersCallback callback) {
        if (!isInitialized()) return;

        if (localInstance.session.getIdentity() == null) {
            callback.onOrdersFetchError(new OrderResultError(OrderResultError.Code.UNIDENTIFIED_TRAVELER), identifier);
            return;
        }

        AuthenticatedUrlRequest request = Router.orders(query, localInstance.session, localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask<OrderResult> fetchTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext,
                        request, new OrderResult.OrderResultMappingFactory());

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
        if (!isInitialized()) return;

        AuthenticatedUrlRequest request = Router.cancellationQuote(order, localInstance.session, localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask<CancellationQuote.Response> fetchTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext,
                        request, new CancellationQuote.Response.ResponseObjectMappingFactory());
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

    public static void cancelOrder(CancellationRequest cancellationRequest, CancellationCallback callback) {
        if (!isInitialized()) return;

        if (cancellationRequest.getCancellationQuote().getExpirationDate().before(new Date())) {
            callback.onCancellationError(new CancellationError(CancellationError.Code.EXPIRED_QUOTE));
            return;
        }

        if (cancellationRequest.getCancellationReason().isExplanationRequired() && TextUtils.isEmpty(cancellationRequest.getExplanation())) {
            callback.onCancellationError(new CancellationError(CancellationError.Code.EXPLANATION_REQUIRED));
            return;
        }

        AuthenticatedUrlRequest request = Router.cancelOrder(cancellationRequest, localInstance.session, localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask<Order> fetchTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext,
                        request, new Order.OrderMappingFactory());
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
        if (!isInitialized()) return;

        AuthenticatedUrlRequest request = Router.emailOrderConfirmation(order, localInstance.session, localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask requestTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext, request, null);
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

    public static void addToWishlist(Product product, WishlistAddCallback callback) {
        if (!isInitialized()) return;
        if (localInstance.session.getIdentity() == null) {
            callback.onWishlistAddError(new WishlistResultError(WishlistResultError.Code.UNIDENTIFIED_TRAVELER));
            return;
        }

        AuthenticatedUrlRequest request = Router.wishlistAdd(product,
                localInstance.session.getIdentity(), localInstance.session, localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask<CatalogItemDetails> requestTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext, request,
                        new BookingItemDetails.BookingItemDetailsObjectMappingFactory());
        BlockTask blockTask = new BlockTask() {
            @Override
            protected void main() {
                if (requestTask.getError() != null) {
                    callback.onWishlistAddError(requestTask.getError());
                } else {
                    callback.onWishlistAddSuccess(product, requestTask.getResource());
                }
            }
        };

        blockTask.addDependency(requestTask);

        localInstance.taskManager.addTask(requestTask);
        TaskManager.getMainTaskManager().addTask(blockTask);
    }

    /**
     * @param item The `Product` that needs to be removed from the wishlist
     * @param originalResult a WishlistResult which should contain every item in productsToRemove
     *
     * @return a nullable WishlistResult with productsToRemove eliminated from its item list.
     *  This immediate return does not represent the state of the server; at a later time
     *  onWishlistRemoveSuccess could be called to confirm the state; or an error callback
     *  may be called with the original WishlistResult
     */
    public static WishlistResult wishlistRemove(Product item, @Nullable WishlistResult originalResult, WishlistRemoveCallback callback) {
        if (!isInitialized()) return originalResult;
        if (localInstance.session.getIdentity() == null) {
            callback.onWishlistRemoveError(new WishlistResultError(WishlistResultError.Code.UNIDENTIFIED_TRAVELER), null);
            return originalResult;
        }

        @Nullable WishlistResult immediateResult = null;

        if (null != originalResult) {
            Set<CatalogItem> immediateItems = new LinkedHashSet<>(originalResult.getItems());
            immediateItems.remove(item);
            immediateResult = new WishlistResult(
                    originalResult.getSkip(),
                    originalResult.getTake(),
                    originalResult.getFromDate(),
                    originalResult.getToDate(),
                    originalResult.getTotal(),
                    immediateItems);
        }

        AuthenticatedUrlRequest request = Router.wishlistRemove(item, localInstance.session.getIdentity(),
                localInstance.session, localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask<CatalogItemDetails> requestTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session, localInstance.applicationContext, request,
                        new BookingItemDetails.BookingItemDetailsObjectMappingFactory());
        BlockTask blockTask = new BlockTask() {
            @Override
            protected void main() {
                if (requestTask.getError() != null) {
                    callback.onWishlistRemoveError(requestTask.getError(), originalResult);
                } else {
                    callback.onWishlistRemoveSuccess(item, requestTask.getResource());
                }
            }
        };

        blockTask.addDependency(requestTask);
        localInstance.taskManager.addTask(requestTask);
        TaskManager.getMainTaskManager().addTask(blockTask);

        return immediateResult;
    }

    /**
     * Fetches an `WishlistResult` corresponding to the given `WishlistQuery`.
     *
     * @param query               The `WishlistQuery` to filter
     * @param identifier          An optional `int` identifying the request. This value is returned back in the callbacks. Use this to distinguish between different requests
     * @param callback            Callback methods to be executed once the results are ready
     */
    public static void fetchWishlist(WishlistQuery query, int identifier, WishlistFetchCallback callback) {
        if (!isInitialized()) return;
        if (localInstance.session.getIdentity() == null) {
            callback.onWishlistFetchError(new WishlistResultError(WishlistResultError.Code.UNIDENTIFIED_TRAVELER), 0);
            return;
        }

        AuthenticatedUrlRequest request = Router.wishlist(
                query,
                localInstance.session.getIdentity(),
                localInstance.session,
                localInstance.applicationContext);
        AuthenticatedRemoteNetworkRequestTask<WishlistResult> fetchTask =
                new AuthenticatedRemoteNetworkRequestTask<>(localInstance.session,
                        localInstance.applicationContext,
                        request, new WishlistResult.WishlistResultMappingFactory());

        class ResultWrapper {
            WishlistResult result;
        }
        final ResultWrapper resultWrapper = new ResultWrapper();

        BlockTask mergeTask = new BlockTask() {
            @Override
            protected void main() {
                if (fetchTask.getResource() == null) {
                    return;
                }

                WishlistResult previousResult = callback.getPreviousResult();
                if (previousResult != null) {
                    resultWrapper.result = previousResult.merge(fetchTask.getResource());
                }

                if (resultWrapper.result == null){
                    resultWrapper.result = fetchTask.getResource();
                }

                callback.onWishlistFetchReceive(resultWrapper.result, identifier);
            }
        };

        BlockTask blockTask = new BlockTask() {
            @Override
            protected void main() {
                if (fetchTask.getError() != null) {
                    callback.onWishlistFetchError(fetchTask.getError(), identifier);
                } else {
                    callback.onWishlistFetchSuccess(fetchTask.getResource(), identifier);
                }
            }
        };

        mergeTask.addDependency(fetchTask);
        blockTask.addDependency(mergeTask);

        localInstance.taskManager.addTask(fetchTask);
        localInstance.orderSerialTaskManager.addTask(mergeTask);
        TaskManager.getMainTaskManager().addTask(blockTask);
    }
}
