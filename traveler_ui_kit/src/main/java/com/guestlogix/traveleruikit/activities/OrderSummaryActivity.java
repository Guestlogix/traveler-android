package com.guestlogix.traveleruikit.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.guestlogix.travelercorekit.callbacks.PaymentConfirmationCallback;
import com.guestlogix.travelercorekit.callbacks.PaymentSaveCallback;
import com.guestlogix.travelercorekit.callbacks.ProcessOrderCallback;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.travelercorekit.models.PaymentError;
import com.guestlogix.travelercorekit.models.Receipt;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.fragments.BillingInformationCollectionFragment;
import com.guestlogix.traveleruikit.fragments.ProductSummaryFragment;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.util.ArrayList;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.ARG_RECEIPT;
import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.REQUEST_CODE_ORDER_FLOW;
import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.RESULT_OK_ORDER_CONFIRMED;

public class OrderSummaryActivity extends AppCompatActivity implements
        ProcessOrderCallback,
        BillingInformationCollectionFragment.OnPaymentMethodChangeListener,
        PaymentConfirmationCallback {

    private static final String TAG = "OrderSummaryActivity";
    public static final String EXTRA_ORDER = "ORDER_SUMMARY_ACTIVITY_EXTRA_ORDER";
    public static final String EXTRA_PAYMENTS = "ORDER_SUMMARY_ACTIVITY_EXTRA_PAYMENTS";
    public static final int CARD_REQUEST = 233;

    private ArrayList<Payment> paymentList;
    private ActionStrip actionStrip;
    private Payment payment;
    private Order order;
    private boolean savePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Bundle extras = getIntent().getExtras();

        if (extras == null || !extras.containsKey(EXTRA_ORDER)) {
            Log.e(TAG, "No Order");
            finish();
            return;
        }

        paymentList = (ArrayList<Payment>) extras.getSerializable(EXTRA_PAYMENTS);

        order = (Order) extras.getSerializable(EXTRA_ORDER);

        // Product Summary.
        ProductSummaryFragment productSummaryFragment =
                (ProductSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_orderSummary_ProductDetails);

        BillingInformationCollectionFragment billingInformationCollectionFragment =
                (BillingInformationCollectionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_orderSummary_billingInformation);

        billingInformationCollectionFragment.setPayments(paymentList);

        actionStrip = findViewById(R.id.actionStrip_orderSummary);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.review_order);
        }

        // Action strip.
        actionStrip.setValue(order.getTotal().getLocalizedDescriptionInBaseCurrency());
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
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);

        Intent orderConfirmationIntent = new Intent(this, OrderConfirmationActivity.class);
        orderConfirmationIntent.putExtra(ARG_RECEIPT, receipt);
        startActivityForResult(orderConfirmationIntent, REQUEST_CODE_ORDER_FLOW);
    }

    @Override
    public void onOrderProcessError(Error error) {
        if (TravelerUI.getPaymentProvider() == null) {
            Log.e(TAG, "No PaymentProvider");
            return;
        }

        // Confirm payment if it is required

        if (error instanceof PaymentError && ((PaymentError) error).getCode() == PaymentError.Code.CONFIRMATION_REQUIRED) {
            TravelerUI.getPaymentProvider().confirmPayment(((PaymentError) error).getData(), this);
            return;
        }

        // Otherwise display error message

        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);

        new AlertDialog.Builder(this)
                .setMessage(error.getMessage())
                .show();

    }

    @Override
    public void onPaymentSelected(Payment payment, boolean savePayment) {
        this.payment = payment;
        this.savePayment = savePayment;

        if (payment != null) {
            actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
        } else {
            actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
        }
    }

    private void onActionStripClick(View _v) {
        actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
        if (payment != null && order != null) {
            if (savePayment) {
                TravelerUI.getPaymentManager().savePayment(payment, new PaymentSaveCallback() {
                    @Override
                    public void onPaymentSaveSuccess() {
                        Traveler.processOrder(order, payment, OrderSummaryActivity.this);
                    }

                    @Override
                    public void onPaymentSaveError(Error error) {
                        Log.w(TAG, "Error saving payment: " + error.getMessage());

                        Traveler.processOrder(order, payment, OrderSummaryActivity.this);
                    }
                });
            } else {
                Traveler.processOrder(order, payment, this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        TravelerUI.getPaymentProvider().onPaymentConfirmationActivityResult(this, requestCode, data, this);

        if (requestCode == REQUEST_CODE_ORDER_FLOW && resultCode == RESULT_OK_ORDER_CONFIRMED) {
            setResult(RESULT_OK_ORDER_CONFIRMED);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPaymentConfirmationSuccess() {
        Traveler.processOrder(order, payment, this);
    }

    @Override
    public void onPaymentConfirmationError(Error error) {
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);

        new AlertDialog.Builder(this)
                .setMessage(error.getMessage())
                .show();
    }
}
