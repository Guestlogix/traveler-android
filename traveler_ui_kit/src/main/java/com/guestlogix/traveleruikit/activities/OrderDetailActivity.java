package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;
import org.w3c.dom.Text;

public class OrderDetailActivity extends AppCompatActivity {
    public static String ARG_ORDER = "ARG_ORDER";

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
        // TODO: Do the price stuff
        //priceTextView.setText();

        LinearLayout productsLayout = findViewById(R.id.layout_orderDetail_products);
        for (Product product : order.getProducts()) {
            View productView = getLayoutInflater().inflate(R.layout.item_order_product, null);
            TextView productTitleTextView = productView.findViewById(R.id.textView_productTitle);
            TextView productDateTextView = productView.findViewById(R.id.textView_productDate);
            TextView productPriceTextView = productView.findViewById(R.id.textView_productPrice);

            productTitleTextView.setText(product.getTitle());
            // TODO: Do the secondary description for product and price
            //productDateTextView.setText();

            // TODO: Product detail page
            productView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            productsLayout.addView(productView);
        }

        Button emailButton = findViewById(R.id.button_orderDetail_emailTickets);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Email tickets
            }
        });
    }
}
