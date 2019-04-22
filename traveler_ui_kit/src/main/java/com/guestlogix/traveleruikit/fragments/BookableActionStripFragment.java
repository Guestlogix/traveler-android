package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Price;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.PassSelectionActivity;
import com.guestlogix.traveleruikit.models.BookingContext;
import com.guestlogix.traveleruikit.models.PurchaseContext;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Fragment to handle Bookable item actions
 */
public class BookableActionStripFragment extends PurchaseFragment implements FetchPassesCallback {
    public static final String BOOKING_CONTEXT_ARG = "arg_booking_context";

    // Views
    private ActionStrip actionStrip;

    // Data
    private BookingContext bookingContext;

    public BookableActionStripFragment() {
        // Do nothing.
    }

    public static BookableActionStripFragment getInstance(BookingContext bookingContext) {
        BookableActionStripFragment f = new BookableActionStripFragment();
        Bundle args = new Bundle();
        args.putSerializable(BOOKING_CONTEXT_ARG, bookingContext);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        View view = inflater.inflate(R.layout.fragment_bookable_action_strip, container, false);
        actionStrip = view.findViewById(R.id.action_container);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
        actionStrip.setActionOnClickListener(this::onActionStripClick);

        if (args != null && args.containsKey(BOOKING_CONTEXT_ARG)) {
            bookingContext = (BookingContext) args.getSerializable(BOOKING_CONTEXT_ARG);
            setup();
        }

        return view;
    }

    @Override
    void setPurchaseContext(PurchaseContext purchaseContext) {
        bookingContext = (BookingContext) purchaseContext;
        setup();
    }

    private void setup() {
        // Set Price
        Price price = bookingContext.getPrice();
        String checkAvailability = getActivityContext().getString(R.string.button_check_availability);
        String startingAt = getActivityContext().getString(R.string.label_starting_at);
        String localizedPrice = String.format(Locale.getDefault(), getActivityContext().getString(R.string.label_price_per_person), price.getFormattedValue());

        actionStrip.setStripValues(checkAvailability, startingAt, localizedPrice);

        // Set state
        PurchaseContext.State state = bookingContext.getState();
        switch (state) {
            case LOADING:
                actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
                break;
            case AVAILABLE:
                actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                break;
            case OPTION_REQUIRED:
            case NOT_AVAILABLE:
            case DEFAULT:
                actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
                break;
            default:
                TravelerLog.w("State not Handled: %s", state.toString());
                break;
        }
    }

    // Submit traveler.
    private void onActionStripClick(View _view) {
        actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
        Traveler.fetchPasses(bookingContext.getProduct(), bookingContext.getAvailability(), bookingContext.getOption(), this);
    }

    @Override
    public void onPassFetchSuccess(List<Pass> pass) {
        Product p = bookingContext.getProduct();
        String flavour = null;

        if (bookingContext.getOption() != null) {
            flavour = bookingContext.getOption().getValue();
        }

        Intent i = new Intent(getActivityContext(), PassSelectionActivity.class);
        i.putExtra(PassSelectionActivity.EXTRA_PRODUCT, p);
        i.putExtra(PassSelectionActivity.EXTRA_PASSES, (Serializable) pass);
        i.putExtra(PassSelectionActivity.EXTRA_FLAVOUR, flavour);
        startActivity(i);
    }

    @Override
    public void onPassFetchError(Error error) {
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
        new AlertDialog.Builder(getActivityContext())
                .setTitle(R.string.unexpected_error)
                .setMessage(error.getMessage())
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();

        PurchaseContext.State state = bookingContext.getState();
        switch (state) {
            case LOADING:
                actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
                break;
            case AVAILABLE:
                actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                break;
            case OPTION_REQUIRED:
            case NOT_AVAILABLE:
            case DEFAULT:
                actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
                break;
            default:
                TravelerLog.w("State not Handled: %s", state.toString());
                break;
        }
    }
}
