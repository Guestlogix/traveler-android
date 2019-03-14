package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.PassSelectionActivity;
import com.guestlogix.traveleruikit.viewmodels.BookableProductViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

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
                case OPTION_NEEDED:
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

        sharedViewModel.getPasses().observe(this, passes -> {
            Product p = sharedViewModel.getProduct();

            Intent i = new Intent(getActivityContext(), PassSelectionActivity.class);
            i.putExtra(PassSelectionActivity.EXTRA_PRODUCT, p);
            i.putExtra(PassSelectionActivity.EXTRA_PASSES, (Serializable) passes);
            startActivity(i);
        });

        actionStrip = view.findViewById(R.id.action_container);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        sharedViewModel.getPrice().observe(this, (price -> {
            String checkAvailability = getString(R.string.button_check_availability);
            String startingAt = getString(R.string.label_starting_at);
            String localizedPrice = String.format(Locale.getDefault(), getString(R.string.label_price_per_person), price.getFormattedValue());

            actionStrip.setStripValues(checkAvailability, startingAt, localizedPrice);
        }));

        actionStrip.setActionOnClickListener(v -> sharedViewModel.submit());
    }
}
