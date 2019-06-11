package com.guestlogix.traveler.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;

public class ProfileActivity extends AppCompatActivity {

    private Profile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = Guest.getInstance().getUserProfile(ProfileActivity.this);
        if (null == user) {
            finish();
            return;
        }

        setContentView(R.layout.activity_profile);
    }
}
