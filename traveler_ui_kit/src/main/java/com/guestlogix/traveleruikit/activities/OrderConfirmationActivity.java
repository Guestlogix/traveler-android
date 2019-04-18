package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.BookableProduct;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Receipt;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;

public class OrderConfirmationActivity extends AppCompatActivity {

    public static final String ARG_RECEIPT = "ARG_RECEIPT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        Bundle extras = getIntent().getExtras();

        if (null == extras || !extras.containsKey(ARG_RECEIPT)) {
            TravelerLog.e("A receipt object is required to run this activity. See ARG_RECEIPT");
            finish();
            return;
        }

        Receipt receipt = (Receipt) extras.getSerializable(ARG_RECEIPT);

        if (receipt == null) {
            TravelerLog.e("A receipt object is required to run this activity. See ARG_RECEIPT");
            finish();
            return;
        }

        TextView titleTextView = findViewById(R.id.textView_orderConfirmation_title);
        TextView subTitleTextView = findViewById(R.id.textView_orderConfirmation_subtitle);
        TextView emailValueTextView = findViewById(R.id.textView_orderConfirmation_emailValue);
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this::navigateHome);

        if (receipt.getProducts().size() > 0) {
            Product product = receipt.getProducts().get(0);

            if(product instanceof BookableProduct){
                BookableProduct bookableProduct = (BookableProduct) product;
                titleTextView.setText(bookableProduct.getTitle());
            }
        }
        subTitleTextView.setText(receipt.getReferenceNumber());
        emailValueTextView.setText(receipt.getCustomerContact().getEmail());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, CatalogItemDetailsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @SuppressWarnings("unused")
    private void navigateHome(View _v) {
        Intent i = TravelerUI.getHomeIntent();
        if (i != null) {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else {
            finish();
        }
    }
}
