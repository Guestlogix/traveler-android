package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.guestlogix.traveleruikit.R;


public class TermsAndConditionsActivity extends AppCompatActivity {
    public static String ARG_CONTENT = "ARG_CONTENT";
    static String TAG = "TermsAndConditionsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getIntent().hasExtra(ARG_CONTENT)) {
            Log.e(TAG, "No content");
            finish();
            return;
        }

        String content = getIntent().getStringExtra(ARG_CONTENT);

        setContentView(R.layout.activity_terms_and_conditions);

        TextView textView = findViewById(R.id.textView_termsAndConditions);
        textView.setText(Html.fromHtml(content));
    }
}
