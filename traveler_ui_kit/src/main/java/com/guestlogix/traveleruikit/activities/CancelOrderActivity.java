package com.guestlogix.traveleruikit.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.CancellationCallback;
import com.guestlogix.travelercorekit.models.CancellationQuote;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.ProductCancellationQuote;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.CancelSuccessFragment;
import com.guestlogix.traveleruikit.fragments.ProgressDialogFragment;

public class CancelOrderActivity extends AppCompatActivity implements CancellationCallback {
    public static String ARG_CANCELLATION_QUOTE = "ARG_CANCELLATION_QUOTE";
    public static String ARG_ORDER = "ARG_ORDER";
    static String TAG = "CancelOrderActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cancel_order);

        CancellationQuote quote = (CancellationQuote) getIntent().getSerializableExtra(ARG_CANCELLATION_QUOTE);
        if (quote == null) {
            Log.e(TAG, "No CancellationQuote");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        TextView totalTextView = findViewById(R.id.textView_cancelOrder_total);
        totalTextView.setText(quote.getTotalRefund().getLocalizedDescriptionInBaseCurrency());

        ViewGroup productsContainer = findViewById(R.id.container_order_products);

        for (ProductCancellationQuote product: quote.getProducts()) {
            View productView = getLayoutInflater().inflate(R.layout.item_product_cancel, productsContainer, false);
            TextView titleTextView = productView.findViewById(R.id.textView_itemProduct_title);
            TextView feeTextView = productView.findViewById(R.id.textView_itemProduct_fee);
            //Button detailsButton = productView.findViewById(R.id.button_itemProduct_details);

            titleTextView.setText(product.getTitle());
            feeTextView.setText(product.getTotalRefund().getLocalizedDescriptionInBaseCurrency());
//            detailsButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(CancelOrderActivity.this, ProductDetailsActivity.class);
//                    intent.putExtra(ProductDetailsActivity.ARG_PRODUCT, product);
//                    startActivity(intent);
//                }
//            });

            productsContainer.addView(productView);
        }

        Button cancelButton = findViewById(R.id.button_cancelOrder_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProgressDialogFragment()
                        .getTransaction(getSupportFragmentManager())
                        .commit();

                Traveler.cancelOrder(quote, CancelOrderActivity.this);
            }
        });
    }

    @Override
    public void onCancellationError(Error error) {
        ProgressDialogFragment
                .findExistingFragment(getSupportFragmentManager())
                .dismiss();

        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(error.getMessage())
                .setNeutralButton("OK", null)
                .show();
    }

    @Override
    public void onCancellationSuccess(Order order) {
        ProgressDialogFragment
                .findExistingFragment(getSupportFragmentManager())
                .dismiss();

        Intent data = new Intent();
        data.putExtra(ARG_ORDER, order);
        setResult(RESULT_OK, data);

        ((ViewGroup) findViewById(R.id.container_cancelOrder)).removeAllViews();

        Fragment fragment = CancelSuccessFragment.getInstance(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_cancelOrder, fragment);
        transaction.commit();
    }
}
