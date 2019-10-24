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
    public static final int RESULT_OK_ORDER_CONFIRMED = -2;
    public static final int REQUEST_CODE_ORDER_FLOW = 1;

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
        findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK_ORDER_CONFIRMED);
                finish();
            }
        });


        if (receipt.getOrder().getProducts().size() > 0) {
            Product product = receipt.getOrder().getProducts().get(0);

            if(product instanceof BookableProduct){
                BookableProduct bookableProduct = (BookableProduct) product;
                titleTextView.setText(bookableProduct.getTitle());
            }
        }
        subTitleTextView.setText(receipt.getOrder().getReferenceNumber());
        emailValueTextView.setText(receipt.getOrder().getContact().getEmail());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, CatalogItemDetailsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}
