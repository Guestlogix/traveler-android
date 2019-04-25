package com.guestlogix.traveleruikit.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.ProcessOrderCallback;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.travelercorekit.models.Receipt;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.BillingInformationCollectionFragment;
import com.guestlogix.traveleruikit.fragments.ProductSummaryFragment;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.ARG_RECEIPT;

public class OrderSummaryActivity extends AppCompatActivity implements
        ProcessOrderCallback,
        BillingInformationCollectionFragment.OnPaymentMethodChangeListener {

    public static final String EXTRA_ORDER = "ORDER_SUMMARY_ACTIVITY_EXTRA_ORDER";
    public static final int CARD_REQUEST = 233;

    private ActionStrip actionStrip;

    // Data
    private Payment payment;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Bundle extras = getIntent().getExtras();

        if (extras == null || !extras.containsKey(EXTRA_ORDER)) {
            TravelerLog.e("OrderSummaryActivity requires an Order object.");
            finish();
            return;
        }

        order = (Order) extras.getSerializable(EXTRA_ORDER);

        if (order == null) {
            TravelerLog.e("OrderSummaryActivity requires an Order object.");
            finish();
            return;
        }

        // Product Summary.
        ProductSummaryFragment productSummaryFragment =
                (ProductSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_orderSummary_ProductDetails);

        BillingInformationCollectionFragment billingInformationCollectionFragment =
                (BillingInformationCollectionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_orderSummary_billingInformation);

        if (productSummaryFragment == null || billingInformationCollectionFragment == null) {
            TravelerLog.e("ProductSummaryFragment or BillingInformationCollectionFragment was not inflated correctly.");
            finish();
            return;
        }

        actionStrip = findViewById(R.id.actionStrip_orderSummary);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);


        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.review_order);
        }

        // Action strip.
        actionStrip.setValue(order.getTotal().getFormattedValue());
        actionStrip.setLabel(getString(R.string.label_price));
        actionStrip.setButtonText(getString(R.string.next));
        actionStrip.setActionOnClickListener(this::onActionStripClick);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        // Product Summary
        productSummaryFragment.setProducts(order.getProducts());

        // Billing info.
        billingInformationCollectionFragment.setOnPaymentMethodChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_summary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuItem_cancel) {
            final Dialog d = new Dialog(this);

            d.setContentView(R.layout.dialog_alert);
            TextView title = d.findViewById(R.id.textView_alertDialog_title);
            TextView msg = d.findViewById(R.id.textView_alertDialog_message);
            Button cancel = d.findViewById(R.id.button_alertDialog_negativeButton);
            Button discard = d.findViewById(R.id.button_alertDialog_positiveButton);

            title.setVisibility(View.GONE);

            msg.setText(R.string.discard_order);

            discard.setText(R.string.discard);
            discard.setOnClickListener(b -> navigateToCatalogItemDetails());

            cancel.setText(R.string.cancel);
            cancel.setOnClickListener(b -> d.dismiss());

            d.show();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    private void navigateToCatalogItemDetails() {
        Intent i = new Intent(this, CatalogItemDetailsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onOrderProcessSuccess(Receipt receipt) {
        Intent orderConfirmationIntent = new Intent(this, OrderConfirmationActivity.class);
        orderConfirmationIntent.putExtra(ARG_RECEIPT, receipt);
        startActivity(orderConfirmationIntent);
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
    }

    @Override
    public void onOrderProcessError(Error error) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.unexpected_error)
                .show();

        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
    }

    @Override
    public void onPaymentAdded(Payment payment) {
        this.payment = payment;
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
    }

    @Override
    public void onPaymentRemoved(Payment payment) {
        if (this.payment == payment) {
            this.payment = null;
            actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
        }
    }

    private void onActionStripClick(View _v) {
        actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
        if (payment != null && order != null) {
            Traveler.processOrder(order, payment, this);
        }
    }
}
