package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.activities.OrderSummaryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment which displays all available payment options and a way too add more.
 * Subscribe to payment add events via {@link OnPaymentMethodChangeListener}
 *
 * <p>Starts a payment collection activity with the registered payment provider.</p>
 */
public class BillingInformationCollectionFragment extends BaseFragment {

    private OnPaymentMethodChangeListener onPaymentMethodChangeListener;

    // Views
    private RecyclerView recyclerView;
    private PaymentsAdapter adapter;
    private Button addCardBtn;

    // Data
    private List<Payment> payments;

    public BillingInformationCollectionFragment() {
        // Do nothing
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new PaymentsAdapter();
        return inflater.inflate(R.layout.fragment_billing_information_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_billingCollection_availableItems);
        payments = new ArrayList<>();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext()));

        addCardBtn = view.findViewById(R.id.button_orderSummary_addCard);
        addCardBtn.setOnClickListener(this::onAddPaymentButtonClick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OrderSummaryActivity.CARD_REQUEST && resultCode == OrderSummaryActivity.RESULT_OK) {
            Bundle extras = data.getExtras();

            if (extras != null && extras.containsKey("RESULT_INTENT_EXTRA_PAYMENT_KEY")) {
                Payment payment = (Payment) extras.getSerializable("RESULT_INTENT_EXTRA_PAYMENT_KEY");

                if (onPaymentMethodChangeListener != null) {
                    onPaymentMethodChangeListener.onPaymentAdded(payment);
                }

                payments.clear();
                payments.add(payment);
                addCardBtn.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Subscribes to payment add events.
     *
     * @param onPaymentMethodChangeListener Callback interface to be invoked whenever a payment is added.
     */
    public void setOnPaymentMethodChangeListener(OnPaymentMethodChangeListener onPaymentMethodChangeListener) {
        this.onPaymentMethodChangeListener = onPaymentMethodChangeListener;
    }

    @Override
    public void onResume() {
        super.onResume();

        addCardBtn.setEnabled(true);
    }

    /**
     * Callback interface for payment change events
     */
    public interface OnPaymentMethodChangeListener {
        /**
         * Invoked whenever a new payment method was added.
         *
         * @param payment Object which was added
         */
        void onPaymentAdded(Payment payment);

        /**
         * Invoked whenever a payment method is deleted.
         *
         * @param payment the payment method which was deleted
         */
        void onPaymentRemoved(Payment payment);
    }

    private void onAddPaymentButtonClick(View _button) {
        if (TravelerUI.getPaymentProvider() != null) {
            addCardBtn.setEnabled(false);
            Intent i = TravelerUI.getPaymentProvider().getPaymentActivityIntent(getActivity());
            startActivityForResult(i, OrderSummaryActivity.CARD_REQUEST);
        }
    }

    private class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {

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
            holder.delete.setTag(position);

            holder.delete.setOnClickListener(v -> {
                int pos = (Integer) v.getTag();
                onPaymentMethodChangeListener.onPaymentRemoved(payments.remove(pos));
                notifyDataSetChanged();
                addCardBtn.setVisibility(View.VISIBLE);
            });
        }

        @Override
        public int getItemCount() {
            return payments != null ? payments.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView label;
            TextView value;
            ImageView delete;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                label = itemView.findViewById(R.id.itemLabel);
                value = itemView.findViewById(R.id.itemValue);
                delete = itemView.findViewById(R.id.imageView_itemLabel_delete);
                delete.setVisibility(View.VISIBLE);
            }
        }
    }
}
