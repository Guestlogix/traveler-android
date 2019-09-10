package com.guestlogix.traveler.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.guestlogix.traveler.R;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.utilities.DateHelper;

public class FlightActivity extends Activity {
    public static String ARG_FLIGHT = "ARG_FLIGHT";
    public static String TAG = "FlightAcitivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_flight_info_card);

        findViewById(R.id.button_flightCard_addFlight).setVisibility(View.INVISIBLE);

        TextView departureCityTextView = findViewById(R.id.textView_flightCard_departureCity);
        TextView departureIATATextView = findViewById(R.id.textView_flightCard_departureIata);
        TextView departureTimeTextView = findViewById(R.id.textView_flightCard_departureTime);
        TextView arrivalCityTextView = findViewById(R.id.textView_flightCard_arrivalCity);
        TextView arrivalIATATextView = findViewById(R.id.textView_flightCard_arrivalIata);
        TextView arrivalTimeTextView = findViewById(R.id.textView_flightCard_arrivalTime);

        if (!getIntent().hasExtra(ARG_FLIGHT)) {
            Log.e(TAG, "No Flight in the intent");
            return;
        }

        Flight flight = (Flight) getIntent().getSerializableExtra(ARG_FLIGHT);

        departureCityTextView.setText(flight.getDepartureAirport().getCity());
        departureIATATextView.setText(flight.getDepartureAirport().getCode());
        departureTimeTextView.setText(flight.getDepartureDateDescription(DateHelper.getTimeFormat()));
        arrivalCityTextView.setText(flight.getArrivalAirport().getCity());
        arrivalIATATextView.setText(flight.getArrivalAirport().getCode());
        arrivalTimeTextView.setText(flight.getArrivalDateDescription(DateHelper.getTimeFormat()));
    }
}
