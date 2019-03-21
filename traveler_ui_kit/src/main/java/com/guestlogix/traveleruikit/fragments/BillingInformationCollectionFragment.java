package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.activities.OrderSummaryActivity;
import com.guestlogix.traveleruikit.viewmodels.OrderSummaryViewModel;

/**
 * A fragment which displays all available payment options and a way too add more.
 */
public class BillingInformationCollectionFragment extends BaseFragment {
    private OrderSummaryViewModel viewModel;

    public BillingInformationCollectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_billing_information_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(OrderSummaryViewModel.class);

        Button button = view.findViewById(R.id.button_orderSummary_addCard);
        button.setOnClickListener(v -> {
            Intent i = TravelerUI.getPaymentProvider().getPaymentActivityIntent(getActivityContext());
            startActivityForResult(i, OrderSummaryActivity.CARD_REQUEST);
        });

        viewModel.getAvailablePayments().observe(this, payments -> {
            // TODO
        });
    }
}
