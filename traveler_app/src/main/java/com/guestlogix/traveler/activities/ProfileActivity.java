package com.guestlogix.traveler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;

public class ProfileActivity extends AppCompatActivity {

    private Profile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = Guest.getInstance().getSignedInUser(ProfileActivity.this);
        if (null == user) {
            finish();
            return;
        }

        setContentView(R.layout.activity_profile);

        CollapsingToolbarLayout layout = findViewById(R.id.collapsingToolbar_profile_title);
        layout.setTitle(String.format("%s %s", user.getFirstName(), user.getLastName()));

        FloatingActionButton fab = findViewById(R.id.fab_profile_editBtn);
        fab.setOnClickListener(this::onEditProfileClick);

        ImageButton settings = findViewById(R.id.imageButton_profile_settings);
        settings.setOnClickListener(this::onSettingsClick);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings_action:
                Intent settingsIntent = new Intent(ProfileActivity.this, AppSettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
        return true;
    }

    private void onEditProfileClick(View fab) {
        // TODO: Launch edit profile.
    }

    private void onSettingsClick(View _settings) {
        Intent settingsIntent = new Intent(ProfileActivity.this, AppSettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void onOrdersClick(View _v) {
        // TODO: When we have a see all orders screen.
    }
}
