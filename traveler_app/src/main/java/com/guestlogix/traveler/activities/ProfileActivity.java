package com.guestlogix.traveler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.traveleruikit.activities.OrdersActivity;
import com.guestlogix.traveleruikit.activities.WishlistActivity;

import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    static final String ARG_PROFILE = "ARG_PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Profile profile = (Profile) getIntent().getSerializableExtra(ARG_PROFILE);

        if (null == profile) {
            Log.e(this.getLocalClassName(), "No Profile in extras");
            finish();
            return;
        }

        setContentView(R.layout.activity_profile);

        setTitle(profile.getFirstName() + " " + profile.getLastName());

        findViewById(R.id.layout_profileOrders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                intent.putExtra(OrdersActivity.ARG_ORDER_QUERY, new OrderQuery(0, 10, null, new Date()));
                startActivity(intent);
            }
        });

        findViewById(R.id.layout_profileSavedList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WishlistActivity.class));
            }
        });

        findViewById(R.id.button_profile_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        ((TextView) findViewById(R.id.textView_profileItem_value)).setText(profile.getEmail());
    }
}
