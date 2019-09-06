package com.guestlogix.traveleruikit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.OrderDetailActivity;
import com.guestlogix.traveleruikit.adapters.OrdersAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class OrdersFragment extends Fragment implements OrdersAdapter.OnItemClickListener {
    public static final String ARG_ORDER_RESULT = "ARG_ORDER_RESULT";
    private static final int REQUEST_CODE_DETAILS = 1;

    private OrderResult orderResult;
    private OrdersAdapter adapter;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param orderResult The `OrderResult` to show.
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

        if (getArguments() == null) {
            Log.e(this.getClass().getName(), "No arguments");
            return;
        }

        orderResult = (OrderResult) getArguments().getSerializable(ARG_ORDER_RESULT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_ordersFragment_orders);

        if (orderResult != null) {
            adapter = new OrdersAdapter(orderResult, this);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e(this.getClass().getName(), "No OrderResult");
        }

        return view;
    }

    // OrdersAdapter.OnItemClickListener

    @Override
    public void onOrderClick(Order order) {
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.ARG_ORDER, order);

        startActivityForResult(intent, REQUEST_CODE_DETAILS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DETAILS && resultCode == Activity.RESULT_OK) {
            Order order = (Order) data.getSerializableExtra(OrderDetailActivity.ARG_ORDER);
            adapter.updateOrder(order);
            adapter.notifyDataSetChanged();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
