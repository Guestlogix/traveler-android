package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.models.BookingForm;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.TravelerErrorFragment;
import com.guestlogix.traveleruikit.viewmodels.BookingViewModel;

import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.*;

/**
 * Activity which encapsulates the whole booking flow.
 * Selecting pass quantities.
 * Supplier questions.
 * <p>
 * Expects a valid booking context.
 */
public class BookingActivity extends AppCompatActivity implements TravelerErrorFragment.OnErrorInteractionListener {

    public static final String ARG_BOOKING_CONTEXT = "booking_context";
    public static final String RESULT_BOOKING_FORM = "booking_form";
    private static final String TAG = "Traveler UI Kit";

    private BookingViewModel bookingViewModel;
    private NavController navController;
    private BookingContext bookingContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Bundle extras = getIntent().getExtras();

        if (null != extras && extras.containsKey(ARG_BOOKING_CONTEXT)) {
            navController = Navigation.findNavController(this, R.id.supplierInformationHostFragment);

            bookingViewModel = ViewModelProviders.of(this).get(BookingViewModel.class);
            bookingViewModel.getObservableStatus().observe(this, this::onStateChange);

            bookingContext = (BookingContext) extras.getSerializable(ARG_BOOKING_CONTEXT);
            bookingViewModel.setBookingContext(bookingContext);
        } else {
            Log.e(TAG, String.format(getString(R.string.no_argument_exception), ARG_BOOKING_CONTEXT, this.getLocalClassName()));
            finish();
        }
    }

    private void onStateChange(BookingViewModel.State state) {
        switch (state) {
            case SUCCESS:
                finishActivityWithResult();
                break;
            case LOADING:
                navController.navigate(R.id.loading_action);
                break;
            case PASS_SELECTION:
                navController.navigate(R.id.pass_selection_action);
                break;
            case QUESTIONS:
                navController.navigate(R.id.supplier_questions_action);
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
        bookingViewModel.setBookingContext(bookingContext);
    }

    private void finishActivityWithResult() {
        BookingForm result = bookingViewModel.getBookingFormObject();

        Intent _result = new Intent();
        _result.putExtra(RESULT_BOOKING_FORM, result);
        setResult(0, _result);
        finish();
    }
}
