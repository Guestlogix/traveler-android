package com.guestlogix.traveler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.traveleruikit.activities.OrdersActivity;

import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    static final String ARG_PROFILE = "ARG_PROFILE";

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profile = (Profile) getIntent().getSerializableExtra(ARG_PROFILE);

        if (null == profile) {
            Log.e(this.getLocalClassName(), "No Profile in extras");
            finish();
            return;
        }

        setContentView(R.layout.activity_profile);

        View ordersLayout = findViewById(R.id.layout_profileOrders);
        ordersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                intent.putExtra(OrdersActivity.EXTRA_ORDER_QUERY, new OrderQuery(0,10,null, new Date()));
                startActivity(intent);
            }
        });


        Button button = findViewById(R.id.button_profile_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        setTitle(profile.getFirstName() + " " + profile.getLastName());

        TextView emailTextView = findViewById(R.id.textView_profileItem_value);
        emailTextView.setText(profile.getEmail());
    }
}
