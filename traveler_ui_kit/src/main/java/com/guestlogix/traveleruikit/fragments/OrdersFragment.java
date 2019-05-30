package com.guestlogix.traveleruikit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.OrdersAdapter;
import com.guestlogix.traveleruikit.widgets.PrefetchRecyclerView;

import java.util.HashSet;
import java.util.Set;

import static com.guestlogix.travelercorekit.models.OrderQuery.DEFAULT_PAGE_SIZE;

public class OrdersFragment extends Fragment {
    private static final String ARG_ORDER_RESULT = "arg_orders";

    private PrefetchRecyclerView ordersRecyclerView;
    private OrderResult ordersResult;
    private volatile OrderResult volatileOrdersResult;
    private OrdersAdapter ordersAdapter;
    private Set<Integer> pagesLoading = new HashSet<>();

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param orderResult .
     * @return A new instance of fragment OrdersFragment.
     */
    public static OrdersFragment newInstance(OrderResult orderResult) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER_RESULT, orderResult);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(ARG_ORDER_RESULT)) {
            ordersResult = (OrderResult) getArguments().getSerializable(ARG_ORDER_RESULT);
            volatileOrdersResult = new OrderResult(ordersResult);
            ordersAdapter = new OrdersAdapter(ordersResult, null);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ordersRecyclerView = view.findViewById(R.id.recyclerView_ordersFragment_orders);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        ordersRecyclerView.setLayoutManager(linearLayoutManager);

        ordersRecyclerView.setAdapter(ordersAdapter);
        ordersRecyclerView.setPrefetchListener(prefetchListener);

        return view;
    }

    private FetchOrdersCallback fetchOrdersCallback = new FetchOrdersCallback() {
        @Override
        public void onOrdersReceived(OrderResult orders, int identifier) {
            volatileOrdersResult = new OrderResult(orders);
        }

        @Override
        public void onOrdersFetchError(Error error, int identifier) {
            pagesLoading.remove(identifier);
            TravelerLog.e("Error while fetching orders");
        }

        @Override
        public void onOrdersFetchSuccess(OrderResult orders, int identifier) {
            pagesLoading.remove(identifier);
            ordersResult = new OrderResult(volatileOrdersResult);
            ordersAdapter.setOrderResult(ordersResult);
            ordersAdapter.notifyItemRangeChanged(identifier * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE);
        }

        @Override
        public OrderResult getPreviousResult() {
            return volatileOrdersResult;
        }
    };

    private PrefetchRecyclerView.PrefetchListener prefetchListener = (indexesRequested, view) -> {

        //filter existing indexes
        Set<Integer> pagesToFetch = new HashSet<>();
        for (int index : indexesRequested) {
            if (!ordersResult.getOrders().containsKey(index)) {

                //calculate pages to be fetched
                int page = index / DEFAULT_PAGE_SIZE;
                //filter if the page is already loading
                if (!pagesLoading.contains(page)) {
                    pagesToFetch.add(page);
                }
            }
        }

        //fetch calculated pages
        for (int page : pagesToFetch) {
            pagesLoading.add(page);
            int offset = page * DEFAULT_PAGE_SIZE;
            int limit = DEFAULT_PAGE_SIZE;
            OrderQuery orderQuery = new OrderQuery(offset, limit, ordersResult.getFromDate(), ordersResult.getToDate());
            Traveler.fetchOrders(orderQuery, page, fetchOrdersCallback);
        }
    };
}
