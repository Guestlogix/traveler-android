package com.guestlogix.traveleruikit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.guestlogix.travelercorekit.callbacks.CancellationQuoteCallback;
import com.guestlogix.travelercorekit.callbacks.EmailOrderConfirmationCallback;
import com.guestlogix.travelercorekit.models.BookableProduct;
import com.guestlogix.travelercorekit.models.CancellationQuote;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderStatus;
import com.guestlogix.travelercorekit.models.Price;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.ProgressDialogFragment;

import org.w3c.dom.Text;

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

            if (product instanceof BookableProduct) {
                BookableProduct bookableProduct = (BookableProduct) product;
                String date = DateHelper.formatToMonthDayYear(bookableProduct.getEventDate());
                String dateTime = date + "\n" + DateHelper.formatTime(bookableProduct.getEventDate());

                productDateTextView.setText(dateTime);

                productPriceTextView.setText(bookableProduct.getPrice().getLocalizedDescriptionInBaseCurrency());
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
        if (order.getStatus() instanceof OrderStatus.Cancelled) {
            cancelButton.setVisibility(View.INVISIBLE);
        } else {
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ProgressDialogFragment()
                            .getTransaction(getSupportFragmentManager())
                            .commit();

                    Traveler.fetchCancellationQuote(order, OrderDetailActivity.this);
                }
            });
        }
    }

    @Override
    public void onCancellationQuoteError(Error error) {
        // TODO: Should this be safe guarded? This is never called without the dialog present
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
}