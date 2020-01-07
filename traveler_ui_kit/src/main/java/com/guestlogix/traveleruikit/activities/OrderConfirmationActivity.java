package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.ProductType;
import com.guestlogix.travelercorekit.models.Receipt;
import com.guestlogix.traveleruikit.R;

public class OrderConfirmationActivity extends AppCompatActivity {
    private static final String TAG = "OrderConfirmationActivi";

    public static final String ARG_RECEIPT = "ARG_RECEIPT";
    public static final int RESULT_OK_ORDER_CONFIRMED = -2;
    public static final int REQUEST_CODE_ORDER_FLOW = 1;
    private Receipt receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        Bundle extras = getIntent().getExtras();

        if (null == extras || !extras.containsKey(ARG_RECEIPT)) {
            Log.e(TAG, "A receipt object is required to run this activity. See ARG_RECEIPT");
            finish();
            return;
        }

        receipt = (Receipt) extras.getSerializable(ARG_RECEIPT);

        if (receipt == null) {
            Log.e(TAG, "A receipt object is required to run this activity. See ARG_RECEIPT");
            finish();
            return;
        }

        TextView titleTextView = findViewById(R.id.textView_orderConfirmation_title);
        TextView subTitleTextView = findViewById(R.id.textView_orderConfirmation_subtitle);
        TextView emailValueTextView = findViewById(R.id.textView_orderConfirmation_emailValue);
        findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK_ORDER_CONFIRMED);
                finish();
            }
        });


        if (receipt.getOrder().getProducts().size() > 0) {
            Product product = receipt.getOrder().getProducts().get(0);
            titleTextView.setText(product.getTitle());
        }
        subTitleTextView.setText(receipt.getOrder().getReferenceNumber());
        emailValueTextView.setText(receipt.getOrder().getContact().getEmail());
    }

    @Override
    public void onBackPressed() {
        //todo: this must be removed. this is not neccesary they can both to home page or finish with a success result and be handled in previuos activities
        if (receipt.getOrder().getProducts().get(0).getProductType() == ProductType.PARTNER_OFFERING) {
            setResult(RESULT_OK_ORDER_CONFIRMED);
            finish();
        }
        else{
            Intent i = new Intent(this, BookingItemDetailsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

}
