package com.guestlogix.traveler.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.traveler.R;

import java.util.ArrayList;
import java.util.List;

public class AppSettingsActivity extends AppCompatActivity {
    List<String> actions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);


    }

}
