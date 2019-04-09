package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
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
    }

    private void onQuestionFormCompleted(BookingForm bookingForm) {
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
        new AlertDialog.Builder(this)
                .setMessage(R.string.unexpected_error)
                .show();
    }
}
