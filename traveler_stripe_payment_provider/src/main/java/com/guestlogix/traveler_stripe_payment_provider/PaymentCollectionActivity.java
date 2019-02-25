package com.guestlogix.traveler_stripe_payment_provider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.guestlogix.travelercorekit.models.Payment;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

public class PaymentCollectionActivity extends AppCompatActivity {

    public static String RESULT_INTENT_EXTRA_PAYMENT_KEY = "RESULT_INTENT_EXTRA_PAYMENT_KEY";
    private Stripe stripe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stripe = new Stripe(this, "pk_test_yUnRnhSqk2DvuL6Qlx9TNrfx");

        setContentView(R.layout.activity_payment_collection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(this::onTokenizeCard);
    }

    private void onTokenizeCard(View button) {
        CardInputWidget cardInputWidget = findViewById(R.id.card_input_widget);
        Card card = cardInputWidget.getCard();

        if (card == null || !card.validateCard() || !card.validateCVC()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Invalid card or CVC");
            builder.setNeutralButton("Retry", null);
            builder.create().show();

            return;
        }

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setVisibility(View.INVISIBLE);

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        cardInputWidget.setEnabled(false);

        stripe.createToken(card, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                doneButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                cardInputWidget.setEnabled(true);

                Toast.makeText(getApplicationContext(),"Could not process credit card.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Token token) {
                Payment payment = new StripePayment(token);

                Intent i = new Intent();
                i.putExtra(RESULT_INTENT_EXTRA_PAYMENT_KEY, payment);

                setResult(RESULT_OK, i);

                finish();
            }
        });


    }
}
