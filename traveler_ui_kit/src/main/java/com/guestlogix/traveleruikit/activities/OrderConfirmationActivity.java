package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.models.BookableProduct;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Receipt;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.OrderConfirmationViewModel;

public class OrderConfirmationActivity extends AppCompatActivity {

    public static final String ARG_RECEIPT = "ARG_RECEIPT";
    OrderConfirmationViewModel orderConfirmationViewModel;

    private TextView titleTextView;
    private TextView subTitleTextView;
    private ImageView confirmationImageView;
    private TextView messageTitleTextView;
    private TextView messageSubTitleTextView;
    private TextView confirmationNumberValueTextView;
    private TextView emailValueTextView;
    private Button viewTicketsButton;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        Bundle extras = getIntent().getExtras();

        if (null != extras && extras.containsKey(ARG_RECEIPT)) {

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

            orderConfirmationViewModel = ViewModelProviders.of(this).get(OrderConfirmationViewModel.class);
            orderConfirmationViewModel.getObservableReceipt().observe(this, this::onReceiptUpdate);

            orderConfirmationViewModel.setReceipt((Receipt) extras.getSerializable(ARG_RECEIPT));

        } else {
            throw new RuntimeException("ARG_RECEIPT is required");
        }
    }

    View.OnClickListener homeOnClickListener = v -> finish();

    private void onReceiptUpdate(Receipt receipt) {
        //update UI

        if (receipt.getProducts().size() > 0) {
            Product product = receipt.getProducts().get(0);

            if(product instanceof BookableProduct){
                BookableProduct bookableProduct = (BookableProduct) product;
                titleTextView.setText(bookableProduct.getTitle());
            }
        }
        subTitleTextView.setText(receipt.getOrderNumber());
        emailValueTextView.setText(receipt.getEmail());
        confirmationNumberValueTextView.setText(receipt.getOrderNumber());
    }
}
