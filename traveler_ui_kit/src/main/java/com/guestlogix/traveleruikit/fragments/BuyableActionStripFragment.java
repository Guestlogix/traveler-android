package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.BookingActivity;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.util.Locale;

import static com.guestlogix.traveleruikit.activities.BookingActivity.EXTRA_BOOKING_CONTEXT;

/**
 * Fragment to handle Buyable item actions
 */
public class BuyableActionStripFragment extends BaseFragment {

    private ActionStrip actionStrip;
    private CatalogItemDetailsViewModel catalogItemDetailsViewModel;

    public BuyableActionStripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookable_action_strip, container, false);

        actionStrip = view.findViewById(R.id.action_container);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catalogItemDetailsViewModel = ViewModelProviders.of(getActivityContext()).get(CatalogItemDetailsViewModel.class);
        catalogItemDetailsViewModel.getObservableActionState().observe(this, this::onActionState);

        actionStrip.setActionOnClickListener(this::onActionSubmit);

        String checkAvailability = getString(R.string.button_buy_now);
        String startingAt = getString(R.string.label_price);
        String price = String.format(Locale.getDefault(), getString(R.string.label_price_per_person), catalogItemDetailsViewModel.getCatalogItemDetails().getPriceStartingAt().getFormattedValue());

        actionStrip.setStripValues(checkAvailability, startingAt, price);
    }

    public void onActionSubmit(View view) {
        if (catalogItemDetailsViewModel.getBookingContext().getSelectedDate() == null) {
            catalogItemDetailsViewModel.setActionState(CatalogItemDetailsViewModel.ActionState.NOT_AVAILABLE);
        } else if (catalogItemDetailsViewModel.getBookingContext().getTimeRequired() && catalogItemDetailsViewModel.getBookingContext().getSelectedTime() == null) {
            catalogItemDetailsViewModel.setActionState(CatalogItemDetailsViewModel.ActionState.TIME_REQUIRED);
        } else {
            onBuyRequest();
        }
    }

    private void onBuyRequest() {

        Intent intent = new Intent(getActivityContext(), BookingActivity.class);
        intent.putExtra(EXTRA_BOOKING_CONTEXT, catalogItemDetailsViewModel.getBookingContext());
        startActivity(intent);
    }

    // Translates VM state to strip state.
    private void onActionState(CatalogItemDetailsViewModel.ActionState state) {
        switch (state) {
            case LOADING:
                actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
                break;
            case NOT_AVAILABLE:
                actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
                break;
            case AVAILABLE:
                actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                break;
            case ERROR:
                actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
                break;
            case TIME_REQUIRED:
                actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
                break;
            default:
                TravelerLog.w("State not Handled: %s", state.toString());

        }
    }

}
