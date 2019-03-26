package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.ProcessOrderCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.BillingInformationCollectionFragment;
import com.guestlogix.traveleruikit.fragments.ProductSummaryFragment;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.ARG_RECEIPT;

public class OrderSummaryActivity extends AppCompatActivity implements ProcessOrderCallback, BillingInformationCollectionFragment.PaymentMethodAdditionListener {

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.review_order);

        // Action strip.
        Price price = order.getTotal();
        actionStrip.setValue(price.getFormattedValue());
        actionStrip.setLabel(getString(R.string.label_price));
        actionStrip.setButtonText(getString(R.string.next));
        actionStrip.setActionOnClickListener(this::onActionStripClick);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        // Product Summary
        productSummaryFragment.setProducts(order.getProducts());

        // Billing info.
        billingInformationCollectionFragment.setPaymentMethodAdditionListener(this);
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
            new AlertDialog.Builder(this)
                    .setMessage(R.string.discard_order)
                    .setPositiveButton(R.string.discard, (x, y) -> OrderSummaryActivity.this.finish())
                    .setNegativeButton(R.string.cancel, (x, y) -> {
                    })
                    .show();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
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

    private void onActionStripClick(View _v) {
        if (payment != null && order != null) {
            Traveler.processOrder(order, payment, this);
        }
    }
}
