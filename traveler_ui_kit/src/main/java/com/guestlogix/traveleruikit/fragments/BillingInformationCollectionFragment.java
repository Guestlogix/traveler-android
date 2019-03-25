package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.activities.OrderSummaryActivity;
import com.guestlogix.traveleruikit.viewmodels.OrderSummaryViewModel;

import java.util.List;

/**
 * A fragment which displays all available payment options and a way too add more.
 * Observes {@link OrderSummaryViewModel} to display the models.
 *
 * <p>Starts a payment collection activity with the registered payment provider.</p>
 */
public class BillingInformationCollectionFragment extends BaseFragment {
    private OrderSummaryViewModel viewModel;
    private RecyclerView recyclerView;
    private PaymentsAdapter adapter;

    public BillingInformationCollectionFragment() {
        // Do nothing
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_billing_information_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_billingCollection_availableItems);

        viewModel = ViewModelProviders.of(getActivityContext()).get(OrderSummaryViewModel.class);
        viewModel.getObservableAvailablePayments().observe(this, this::onPaymentsChanged);


        Button button = view.findViewById(R.id.button_orderSummary_addCard);
        button.setOnClickListener(this::onAddPaymentButtonClick);
    }

    private void onPaymentsChanged(List<Payment> payments) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext()));

        if (adapter == null) {
            adapter = new PaymentsAdapter();
            recyclerView.setAdapter(adapter);
        }

        adapter.payments = payments;

        adapter.notifyDataSetChanged();
    }

    private void onAddPaymentButtonClick(View _button) {
        if (TravelerUI.getPaymentProvider() != null) {
            Intent i = TravelerUI.getPaymentProvider().getPaymentActivityIntent(getActivity());
            startActivityForResult(i, OrderSummaryActivity.CARD_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OrderSummaryActivity.CARD_REQUEST && resultCode == OrderSummaryActivity.RESULT_OK) {
            Bundle extras = data.getExtras();

            // TODO find a better way of getting the key.
            if (extras != null && extras.containsKey("RESULT_INTENT_EXTRA_PAYMENT_KEY")) {
                Payment payment = (Payment) extras.getSerializable("RESULT_INTENT_EXTRA_PAYMENT_KEY");
                viewModel.setPayment(payment);
            }
        }
    }

    private class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {
        List<Payment> payments;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.item_label_value, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Payment payment = payments.get(position);
            List<Attribute> attributes = payment.getAttributes();
            Attribute a = attributes.get(0);

            holder.label.setText(a.getLabel());
            holder.value.setText(a.getValue());
        }

        @Override
        public int getItemCount() {
            return payments != null ? payments.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView label;
            TextView value;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                label = itemView.findViewById(R.id.itemLabel);
                value = itemView.findViewById(R.id.itemValue);
            }
        }
    }
}
