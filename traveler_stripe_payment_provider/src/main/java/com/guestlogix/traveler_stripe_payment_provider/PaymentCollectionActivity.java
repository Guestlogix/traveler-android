package com.guestlogix.traveler_stripe_payment_provider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.travelercorekit.models.Traveler;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

public class PaymentCollectionActivity extends AppCompatActivity {

    public static String RESULT_INTENT_EXTRA_PAYMENT_KEY = "RESULT_INTENT_EXTRA_PAYMENT_KEY";
    private Stripe stripe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stripe = new Stripe(this, Traveler.isSandboxMode() ?
                BuildConfig.STRIPE_PUBLISHABLE_KEY_SANDBOX : BuildConfig.STRIPE_PUBLISHABLE_KEY_NON_SANDBOX);

        setContentView(R.layout.activity_payment_collection);
        setTitle(R.string.title_payment_collection_Activity);

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(this::onTokenizeCard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void onTokenizeCard(View button) {
        CardInputWidget cardInputWidget = findViewById(R.id.card_input_widget);
        Card card = cardInputWidget.getCard();

        if (card == null || !card.validateCard() || !card.validateCVC()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.invalid_card_or_cvc));
            builder.setNeutralButton(getString(R.string.retry), null);
            builder.create().show();

            return;
        }

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setVisibility(View.INVISIBLE);

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        cardInputWidget.setEnabled(false);

        stripe.createPaymentMethod(PaymentMethodCreateParams.create(card.toPaymentMethodParamsCard()),
                new ApiResultCallback<PaymentMethod>() {
                    @Override
                    public void onSuccess(@NonNull PaymentMethod result) {
                        Payment payment = new StripePayment(result);

                        Intent i = new Intent();
                        i.putExtra(RESULT_INTENT_EXTRA_PAYMENT_KEY, payment);

                        setResult(RESULT_OK, i);

                        finish();
                    }

                    @Override
                    public void onError(@NonNull Exception e) {
                        doneButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        cardInputWidget.setEnabled(true);

                        Toast.makeText(getApplicationContext(),
                                getString(R.string.could_not_process_cc) + " :\n" + e.getLocalizedMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
