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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.guestlogix.travelercorekit.callbacks.OrderCreateCallback;
import com.guestlogix.travelercorekit.models.PurchaseForm;
import com.guestlogix.travelercorekit.callbacks.PaymentsFetchCallback;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.widgets.PurchaseFormWidget;
import com.guestlogix.traveleruikit.TravelerUI;

import java.util.ArrayList;
import java.util.List;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.REQUEST_CODE_ORDER_FLOW;
import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.RESULT_OK_ORDER_CONFIRMED;

public class QuestionsActivity extends AppCompatActivity implements OrderCreateCallback {
    private static final String TAG = "QuestionsActivity";
    public static final String EXTRA_PURCHASE_FORM = "EXTRA_QUESTIONS_ACTIVITY_PURCHASE_FORM";

    // Views
    private PurchaseFormWidget form;

    // Data
    private PurchaseForm purchaseForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_PURCHASE_FORM)) {
            purchaseForm = (PurchaseForm) savedInstanceState.getSerializable(EXTRA_PURCHASE_FORM);
        }

        if (purchaseForm == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null || !extras.containsKey(EXTRA_PURCHASE_FORM)) {
                Log.e(TAG, "QuestionsActivity requires a PurchaseForm to operate.");
                finish();
                return;
            }

            purchaseForm = (PurchaseForm) extras.getSerializable(EXTRA_PURCHASE_FORM);
        }


        if (purchaseForm == null) {
            Log.e(TAG, "QuestionsActivity requires a PurchaseForm to operate.");
            finish();
            return;
        }

        form = findViewById(R.id.questionForm_questionsActivity);
        form.setPurchaseForm(purchaseForm);
        form.setLayoutManager(new LinearLayoutManager(this));
        form.setFormCompletedListener(this::onQuestionFormCompleted);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void onQuestionFormCompleted(PurchaseForm purchaseForm) {
        form.setFormCompletedListener(null);
        // TODO: Must show a spinner or something UI blocking until this is done
        Traveler.createOrder(purchaseForm, this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(EXTRA_PURCHASE_FORM, purchaseForm);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onOrderCreateSuccess(Order order) {
        TravelerUI.getPaymentManager().fetchPayments(new PaymentsFetchCallback() {
            @Override
            public void onPaymentsFetchSuccess(List<Payment> paymentList) {
                advanceToSummary(new ArrayList<>(paymentList));
            }

            @Override
            public void onPaymentsFetchError(Error error) {
                // Graceful failure: In case we can't fetch saved payments we still
                // want to advance the user
                advanceToSummary(new ArrayList<>());
            }

            private void advanceToSummary(ArrayList<Payment> payments) {
                Intent intent = new Intent(QuestionsActivity.this, OrderSummaryActivity.class);
                intent.putExtra(OrderSummaryActivity.EXTRA_ORDER, order);
                intent.putExtra(OrderSummaryActivity.EXTRA_PAYMENTS, payments);
                startActivityForResult(intent, REQUEST_CODE_ORDER_FLOW);
            }
        });
    }

    @Override
    public void onOrderCreateFailure(Error error) {
        form.setFormCompletedListener(this::onQuestionFormCompleted);
        new AlertDialog.Builder(this)
                .setMessage(R.string.unexpected_error)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (form != null) {
            form.setFormCompletedListener(this::onQuestionFormCompleted);
        }
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
            discard.setOnClickListener(b -> {
                d.dismiss();
                Intent i = new Intent(this, BookingItemDetailsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            });

            cancel.setText(R.string.cancel);
            cancel.setOnClickListener(b -> d.dismiss());

            d.show();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ORDER_FLOW && resultCode == RESULT_OK_ORDER_CONFIRMED) {
            setResult(RESULT_OK_ORDER_CONFIRMED);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
