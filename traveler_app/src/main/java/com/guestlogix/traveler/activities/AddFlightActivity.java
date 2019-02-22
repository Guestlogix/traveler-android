package com.guestlogix.traveler.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.viewmodels.SearchFlightResultViewModel;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.*;

public class AddFlightActivity extends AppCompatActivity {
    private NavController navController;
    private SearchFlightResultViewModel searchFlightResultViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);

        navController = Navigation.findNavController(findViewById(R.id.flightSearchHostFragment));
        searchFlightResultViewModel = ViewModelProviders.of(this).get(SearchFlightResultViewModel.class);

        searchFlightResultViewModel.getStatus().observe(this, this::flightStateChangeHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_flight_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_action:
                finish();
                return true;
            default:
                return false;
        }
    }

    private void flightStateChangeHandler(StatefulViewModel.State state) {
        switch (state) {
            case LOADING:
                navController.navigate(R.id.loading_action);
                break;
            case SUCCESS:
                navController.navigate(R.id.flight_search_result_action);
                break;
            case ERROR:
                Bundle arguments = new Bundle();
                arguments.putString(ARG_ERROR_TITLE, getString(com.guestlogix.traveleruikit.R.string.label_sorry));
                arguments.putString(ARG_ERROR_MESSAGE, getString(com.guestlogix.traveleruikit.R.string.label_nothing_to_show));
                arguments.putString(ARG_ERROR_ACTION, getString(com.guestlogix.traveleruikit.R.string.try_again));

                navController.navigate(R.id.error_action, arguments);
                break;
        }
    }
}
