package com.guestlogix.traveler.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.guestlogix.traveler.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Traveler.getCatalog("1", new Traveler.CatalogResponseHandler() {
//            @Override
//            public void onSuccess(Catalog catalog) {
//                Log.d("MainActivity", "onSuccess()");
//            }
//
//            @Override
//            public void onError(Error e) {
//                Log.d("MainActivity", "onError()");
//            }
//        });
    }
}
