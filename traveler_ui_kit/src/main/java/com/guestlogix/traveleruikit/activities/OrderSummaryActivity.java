package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.fragments.ProductSummaryFragment;
import com.guestlogix.traveleruikit.viewmodels.OrderSummaryViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

public class OrderSummaryActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER = "ORDER_SUMMARY_ACTIVITY_EXTRA_ORDER";
    private static final int CARD_REQUEST = 233;

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
                    actionStrip.setButtonText("Next");
                });
            }
        }

        Button button = findViewById(R.id.button_orderSummary_addCard);
        button.setOnClickListener(v -> {
            Intent i = TravelerUI.getPaymentProvider().getPaymentActivityIntent(OrderSummaryActivity.this);
            startActivityForResult(i, CARD_REQUEST);
        });
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
                    .setMessage("Discard this order?")
                    .show(); //TODO
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CARD_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();

                if (extras != null && extras.containsKey("RESULT_INTENT_EXTRA_PAYMENT_KEY")) {
                    Payment payment = (Payment) extras.getSerializable("RESULT_INTENT_EXTRA_PAYMENT_KEY");
                    viewModel.setPayment(payment);
                }
            }
        }
    }
}
