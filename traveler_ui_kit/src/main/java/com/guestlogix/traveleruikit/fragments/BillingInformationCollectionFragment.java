package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    private static final String BUNDLE_PAYMENT = "payment";

    // Views
    private RecyclerView recyclerView;
    private PaymentsAdapter adapter;
    private Button addCardBtn;
    private CheckBox savePaymentCheckbox;

    // Data
    private ArrayList<Payment> payments;
    private ArrayList<Payment> savedPayments;

    public BillingInformationCollectionFragment() {
        // Do nothing
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.payments = new ArrayList<>();
        this.savedPayments = new ArrayList<>();

        if (null != savedInstanceState && savedInstanceState.containsKey(BUNDLE_PAYMENT)) {
            this.payments = (ArrayList<Payment>) savedInstanceState.getSerializable(BUNDLE_PAYMENT);
            this.savedPayments = (ArrayList<Payment>) savedInstanceState.getSerializable(BUNDLE_PAYMENT);
        }
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
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext()));

        addCardBtn = view.findViewById(R.id.button_orderSummary_addCard);
        addCardBtn.setOnClickListener(this::onAddPaymentButtonClick);

        savePaymentCheckbox = view.findViewById(R.id.checkbox_billingCollection_savePayment);
        savePaymentCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (adapter.selectedIndex == -1) {
                    return;
                }

                Payment payment = payments.get(adapter.selectedIndex);

                if (payment != null) {
                    setSelectedPayment(payment);
                }
            }
        });

        if (!payments.isEmpty()) {
            addCardBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OrderSummaryActivity.CARD_REQUEST && resultCode == OrderSummaryActivity.RESULT_OK) {
            Bundle extras = data.getExtras();

            if (extras != null && extras.containsKey("RESULT_INTENT_EXTRA_PAYMENT_KEY")) {
                Payment payment = (Payment) extras.getSerializable("RESULT_INTENT_EXTRA_PAYMENT_KEY");

                payments.add(payment);
                addCardBtn.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();

                setSelectedPayment(payment);
            }
        }
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
        adapter.notifyDataSetChanged();
    }

    private void setSelectedPayment(Payment payment) {
        boolean isPreviouslySaved = savedPayments.contains(payment);

        savePaymentCheckbox.setVisibility(isPreviouslySaved ? View.INVISIBLE : View.VISIBLE);

        if (onPaymentMethodChangeListener != null) {
            onPaymentMethodChangeListener.onPaymentSelected(payment, savePaymentCheckbox.isChecked() && !isPreviouslySaved);
        }
    }

    /**
     * Subscribes to payment add events.
     *
     * @param l Callback interface to be invoked whenever a payment is added.
     */
    public void setOnPaymentMethodChangeListener(OnPaymentMethodChangeListener l) {
        this.onPaymentMethodChangeListener = l;

        if (l != null && !payments.isEmpty()) {
            setSelectedPayment(payments.get(0));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (payments.size() > 0) {
            addCardBtn.setVisibility(View.GONE);
        } else {
            addCardBtn.setVisibility(View.VISIBLE);
        }

        addCardBtn.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!payments.isEmpty()) {
            outState.putSerializable(BUNDLE_PAYMENT, payments); // Only one for now
        }
    }

    /**
     * Callback interface for payment change events
     */
    public interface OnPaymentMethodChangeListener {
        void onPaymentSelected(Payment payment, boolean savePayment);
    }

    private void onAddPaymentButtonClick(View _button) {
        if (TravelerUI.getPaymentProvider() != null) {
            addCardBtn.setEnabled(false);
            Intent i = TravelerUI.getPaymentProvider().getPaymentActivityIntent(getActivity());
            startActivityForResult(i, OrderSummaryActivity.CARD_REQUEST);
        }
    }

    private class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {
        private int selectedIndex = -1;

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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedIndex = position;

                    adapter.notifyDataSetChanged();

                    setSelectedPayment(payment);
                }
            });

            holder.delete.setOnClickListener(v -> {
                int pos = (Integer) v.getTag();
                notifyDataSetChanged();
                setSelectedPayment(null);
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
