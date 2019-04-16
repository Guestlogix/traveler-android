package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.BookableProduct;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Receipt;
import com.guestlogix.traveleruikit.R;

public class OrderConfirmationActivity extends AppCompatActivity {

    public static final String ARG_RECEIPT = "ARG_RECEIPT";

    // Views
    private TextView titleTextView;
    private TextView subTitleTextView;
    private ImageView confirmationImageView;
    private TextView messageTitleTextView;
    private TextView messageSubTitleTextView;
    private TextView confirmationNumberValueTextView;
    private TextView emailValueTextView;
    private Button viewTicketsButton;
    private Button homeButton;

    // Data
    private Receipt receipt;

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

        receipt = (Receipt) extras.getSerializable(ARG_RECEIPT);

        titleTextView = findViewById(R.id.titleTextView);
        subTitleTextView = findViewById(R.id.subTitleTextView);
        confirmationImageView = findViewById(R.id.confirmationImageView);
        messageTitleTextView = findViewById(R.id.messageTitleTextView);
        messageSubTitleTextView = findViewById(R.id.messageSubTitleTextView);
        confirmationNumberValueTextView = findViewById(R.id.confirmationNumberValueTextView);
        emailValueTextView = findViewById(R.id.emailValueTextView);
        viewTicketsButton = findViewById(R.id.viewTicketsButton);
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(homeOnClickListener);

        if (receipt.getProducts().size() > 0) {
            Product product = receipt.getProducts().get(0);

            if(product instanceof BookableProduct){
                BookableProduct bookableProduct = (BookableProduct) product;
                titleTextView.setText(bookableProduct.getTitle());
            }
        }
        subTitleTextView.setText(receipt.getReferenceNumber());
        emailValueTextView.setText(receipt.getEmail());
        confirmationNumberValueTextView.setText(receipt.getReferenceNumber());
    }

    View.OnClickListener homeOnClickListener = v -> finish();
}
