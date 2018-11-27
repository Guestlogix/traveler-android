package com.guestlogix.traveler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.guestlogix.travelercorekit.sdk.Traveler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Traveler.initialize("apiKey", applicationContext)

    }
}
