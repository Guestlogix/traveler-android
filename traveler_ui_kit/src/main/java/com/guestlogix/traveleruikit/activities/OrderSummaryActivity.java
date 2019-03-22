package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.OrderSummaryViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.ARG_RECEIPT;

public class OrderSummaryActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER = "ORDER_SUMMARY_ACTIVITY_EXTRA_ORDER";
    public static final int CARD_REQUEST = 233;

    private OrderSummaryViewModel viewModel;
    private ActionStrip actionStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        Bundle extras = getIntent().getExtras();
        actionStrip = findViewById(R.id.actionStrip_orderSummary);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.review_order);

        if (extras != null && extras.containsKey(EXTRA_ORDER)) {
            Order order = (Order) extras.getSerializable(EXTRA_ORDER);

            if (null != order) {

                viewModel = ViewModelProviders.of(this).get(OrderSummaryViewModel.class);
                viewModel.setup(order);
                viewModel.getDisplayPrice().observe(this, price -> {
                    actionStrip.setValue(price.getFormattedValue());
                    actionStrip.setLabel(getString(R.string.label_price));
                    actionStrip.setButtonText(getString(R.string.next));
                });

                viewModel.getState().observe(this, state -> {
                    switch (state) {
                        case LOADING:
                            actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
                            break;
                        case DEFAULT:
                            actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
                            break;
                        case READY:
                            actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                            break;
                        case ERROR:
                            actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                            new AlertDialog.Builder(OrderSummaryActivity.this)
                                    .setMessage(R.string.unknown_error_message)
                                    .show();
                    }
                });

                actionStrip.setActionOnClickListener(v -> viewModel.submit());

                viewModel.getReceipt().observe(this, receipt -> {
                    // TODO: Launch next activity
                    Intent orderConfirmationIntent = new Intent(OrderSummaryActivity.this, OrderConfirmationActivity.class);
                    orderConfirmationIntent.putExtra(ARG_RECEIPT, receipt);
                    startActivity(orderConfirmationIntent);
                });

                return;
            }
        }

        TravelerLog.e("OrderSummaryActivity requires an Order object to operate.");
        finish();
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

}
