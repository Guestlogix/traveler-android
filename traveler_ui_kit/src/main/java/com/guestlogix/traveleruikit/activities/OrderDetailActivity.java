package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.guestlogix.travelercorekit.callbacks.CancellationQuoteCallback;
import com.guestlogix.travelercorekit.callbacks.EmailOrderConfirmationCallback;
import com.guestlogix.travelercorekit.models.BookingProduct;
import com.guestlogix.travelercorekit.models.CancellationQuote;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderStatus;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.ProgressDialogFragment;

public class OrderDetailActivity extends AppCompatActivity implements CancellationQuoteCallback, EmailOrderConfirmationCallback {
    public static String ARG_ORDER = "ARG_ORDER";

    private static int REQUEST_CODE_CANCEL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);

        Order order = (Order) getIntent().getSerializableExtra(ARG_ORDER);
        if (order == null) {
            Log.e(this.getLocalClassName(), "No Order");
            finish();
            return;
        }

        TextView titleTextView = findViewById(R.id.textView_orderDetail_title);
        TextView dateTextView = findViewById(R.id.textView_orderDetail_date);
        TextView priceTextView = findViewById(R.id.textView_orderDetail_price);

        titleTextView.setText(order.getId());
        dateTextView.setText(DateHelper.formatToMonthDayYear(order.getCreatedDate()));
        priceTextView.setText(order.getTotal().getLocalizedDescriptionInBaseCurrency());

        OrderDetailActivity self = this;

        // TODO: Set title?

        LinearLayout productsLayout = findViewById(R.id.layout_orderDetail_products);
        for (Product product : order.getProducts()) {
            View productView = getLayoutInflater().inflate(R.layout.item_order_product, null);
            TextView productTitleTextView = productView.findViewById(R.id.textView_productTitle);
            TextView productDateTextView = productView.findViewById(R.id.textView_productDate);
            TextView productPriceTextView = productView.findViewById(R.id.textView_productPrice);

            productTitleTextView.setText(product.getTitle());

            if (product instanceof BookingProduct) {
                BookingProduct bookingProduct = (BookingProduct) product;
                String date = DateHelper.formatToMonthDayYear(bookingProduct.getEventDate());
                String dateTime = date + "\n" + DateHelper.formatTime(bookingProduct.getEventDate());

                productDateTextView.setText(dateTime);

                productPriceTextView.setText(bookingProduct.getPrice().getLocalizedDescriptionInBaseCurrency());
            }

            productView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(self, ProductDetailsActivity.class);
                    intent.putExtra(ProductDetailsActivity.ARG_PRODUCT, product);
                    startActivity(intent);
                }
            });

            productsLayout.addView(productView);
        }

        Button emailButton = findViewById(R.id.button_orderDetail_emailTickets);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProgressDialogFragment()
                        .getTransaction(getSupportFragmentManager())
                        .commit();

                Traveler.emailOrderConfirmation(order, OrderDetailActivity.this);
            }
        });

        Button cancelButton = findViewById(R.id.button_orderDetail_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProgressDialogFragment()
                        .getTransaction(getSupportFragmentManager())
                        .commit();

                Traveler.fetchCancellationQuote(order, OrderDetailActivity.this);
            }
        });

        reloadState(order);
    }

    private void reloadState(Order order) {
        Button cancelButton = findViewById(R.id.button_orderDetail_cancel);
        cancelButton.setVisibility(order.getStatus() instanceof OrderStatus.Cancelled ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onCancellationQuoteError(Error error) {
        // TODO: Should this be safe guarded? This is never called without the dialog present
        ProgressDialogFragment.findExistingFragment(getSupportFragmentManager())
                .dismiss();

        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(error.getMessage())
                .setNeutralButton("OK", null)
                .show();
    }

    @Override
    public void onCancellationQuoteSuccess(CancellationQuote quote) {
        ProgressDialogFragment.findExistingFragment(getSupportFragmentManager())
                .dismiss();

        Intent intent = new Intent(getApplicationContext(), CancelOrderActivity.class);
        intent.putExtra(CancelOrderActivity.ARG_CANCELLATION_QUOTE, quote);
        startActivityForResult(intent, REQUEST_CODE_CANCEL);
    }

    @Override
    public void onEmailError(Error error) {
        ProgressDialogFragment.findExistingFragment(getSupportFragmentManager())
                .dismiss();

        // TODO: Better error messaging

        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Something went wrong")
                .setNeutralButton("OK", null)
                .show();
    }

    @Override
    public void onEmailSuccess() {
        ProgressDialogFragment.findExistingFragment(getSupportFragmentManager())
                .dismiss();

        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your tickets have been emailed.")
                .setNeutralButton("OK", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CANCEL && resultCode == RESULT_OK) {
            Order order = (Order) data.getSerializableExtra(CancelOrderActivity.ARG_ORDER);

            reloadState(order);

            Intent newData = new Intent();
            newData.putExtra(ARG_ORDER, order);
            setResult(RESULT_OK, newData);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
