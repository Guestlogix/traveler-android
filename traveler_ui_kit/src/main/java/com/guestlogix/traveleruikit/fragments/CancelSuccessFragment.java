package com.guestlogix.traveleruikit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.traveleruikit.R;

public class CancelSuccessFragment extends Fragment {
    public static String ARG_ORDER = "ARG_ORDER";
    static String TAG = "CancelSuccessFragment";

    public static CancelSuccessFragment getInstance(Order order) {
        CancelSuccessFragment fragment = new CancelSuccessFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancellation_success, container, false);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_ORDER)) {
            Log.e(TAG, "No Order");
            return view;
        }

        Order order = (Order) args.get(ARG_ORDER);

        TextView orderNumberTextView = view.findViewById(R.id.textView_cancelSuccess_orderNumber);
        orderNumberTextView.setText(order.getId());

        return view;
    }
}
