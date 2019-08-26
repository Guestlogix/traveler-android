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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.OrderCreateCallback;
import com.guestlogix.travelercorekit.models.BookingForm;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.widgets.BookingFormWidget;

public class QuestionsActivity extends AppCompatActivity implements OrderCreateCallback {
    public static final String EXTRA_BOOKING_FORM = "EXTRA_QUESTIONS_ACTIVITY_BOOKING_FORM";

    // Views
    private BookingFormWidget form;

    // Data
    private BookingForm bookingForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_BOOKING_FORM)) {
            bookingForm = (BookingForm) savedInstanceState.getSerializable(EXTRA_BOOKING_FORM);
        }

        if (bookingForm == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null || !extras.containsKey(EXTRA_BOOKING_FORM)) {
                TravelerLog.e("QuestionsActivity requires a BookingForm to operate.");
                finish();
                return;
            }

            bookingForm = (BookingForm) extras.getSerializable(EXTRA_BOOKING_FORM);
        }


        if (bookingForm == null) {
            TravelerLog.e("QuestionsActivity requires a BookingForm to operate.");
            finish();
            return;
        }

        form = findViewById(R.id.questionForm_questionsActivity);
        form.setBookingForm(bookingForm);
        form.setLayoutManager(new LinearLayoutManager(this));
        form.setFormCompletedListener(this::onQuestionFormCompleted);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void onQuestionFormCompleted(BookingForm bookingForm) {
        form.setFormCompletedListener(null);
        // TODO: Must show a spinner or something UI blocking until this is done
        Traveler.createOrder(bookingForm, this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(EXTRA_BOOKING_FORM, bookingForm);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onOrderCreateSuccess(Order order) {
        Intent intent = new Intent(this, OrderSummaryActivity.class);
        intent.putExtra(OrderSummaryActivity.EXTRA_ORDER, order);
        startActivity(intent);
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
                Intent i = new Intent(this, CatalogItemDetailsActivity.class);
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
}
