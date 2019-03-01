package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.BookingActivity;
import com.guestlogix.traveleruikit.viewmodels.BookableProductViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.util.Locale;

import static com.guestlogix.traveleruikit.activities.BookingActivity.EXTRA_BOOKING_CONTEXT;

/**
 * Fragment to handle Bookable item actions
 */
public class BookableActionStripFragment extends BaseFragment {

    private ActionStrip actionStrip;
    private BookableProductViewModel sharedViewModel;

    public BookableActionStripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookable_action_strip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = ViewModelProviders.of(getActivityContext()).get(BookableProductViewModel.class);
        sharedViewModel.getAvailabilityState().observe(this, (state -> {
            switch (state) {
                case LOADING:
                    actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
                    break;
                case AVAILABLE:
                    actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                    break;
                case TIME_REQUIRED:
                case NOT_AVAILABLE:
                case ERROR:
                case DEFAULT:
                    actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
                    break;
                default:
                    TravelerLog.w("State not Handled: %s", state.toString());
                    break;
            }
        }));

        actionStrip = view.findViewById(R.id.action_container);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        sharedViewModel.getPrice().observe(this, (price -> {
            String checkAvailability = getString(R.string.button_check_availability);
            String startingAt = getString(R.string.label_starting_at);
            String localizedPrice = String.format(Locale.getDefault(), getString(R.string.label_price_per_person), price.getFormattedValue());

            actionStrip.setStripValues(checkAvailability, startingAt, localizedPrice);
        }));

        actionStrip.setActionOnClickListener((v) -> {
            // Launch the next activity.
            Intent intent = new Intent(getActivityContext(), BookingActivity.class);
            intent.putExtra(EXTRA_BOOKING_CONTEXT, sharedViewModel.getBookingContext());
            startActivity(intent);
        });
    }
}
