package com.guestlogix.traveleruikit.activities;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.TravelerErrorFragment;
import com.guestlogix.viewmodels.StatefulViewModel;
import com.guestlogix.viewmodels.SupplierQuestionsViewModel;

import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.ARG_ERROR_ACTION;
import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.ARG_ERROR_MESSAGE;
import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.ARG_ERROR_TITLE;

public class SupplierQuestionsActivity extends AppCompatActivity implements TravelerErrorFragment.OnErrorInteractionListener {

    public static final String ARG_BOOKING_CONTEXT = "booking_context";
    private static final String TAG = "Traveler UI Kit";

    private SupplierQuestionsViewModel questionsViewModel;
    private NavController navController;
    private BookingContext bookingContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_questions);

        Bundle extras = getIntent().getExtras();

        if (null != extras && extras.containsKey(ARG_BOOKING_CONTEXT)) {
            navController = Navigation.findNavController(this, R.id.supplierInformationHostFragment);

            questionsViewModel = ViewModelProviders.of(this).get(SupplierQuestionsViewModel.class);
            questionsViewModel.getStatus().observe(this, this::onStateChange);

            bookingContext = (BookingContext) extras.getSerializable(ARG_BOOKING_CONTEXT);
            questionsViewModel.setBookingContext(bookingContext);
        } else {
            Log.e(TAG, String.format(getString(R.string.no_argument_exception), ARG_BOOKING_CONTEXT, this.getLocalClassName()));
            finish();
        }
    }

    private void onStateChange(StatefulViewModel.State state) {
        switch (state) {
            case LOADING:
                navController.navigate(R.id.loading_action);
                break;
            case SUCCESS:
                navController.navigate(R.id.pass_selection_action);
                break;
            case ERROR:
                Bundle arguments = new Bundle();
                arguments.putString(ARG_ERROR_TITLE, getString(R.string.label_sorry));
                arguments.putString(ARG_ERROR_MESSAGE, getString(R.string.label_nothing_to_show));
                arguments.putString(ARG_ERROR_ACTION, getString(R.string.try_again));

                navController.navigate(R.id.error_action, arguments);
                break;
        }
    }

    @Override
    public void onRetry() {
        questionsViewModel.setBookingContext(bookingContext);
    }
}
