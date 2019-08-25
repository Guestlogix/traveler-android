package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Price;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.activities.AvailabilityActivity;
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
public class BookableActionStripFragment extends Fragment {
    public static final String ARG_BOOKING_CONTEXT = "ARG_BOOKING_CONTEXT";
    public static final String TAG = "BookableActionStrip";

    private int containerId;

    public static BookableActionStripFragment newInstance(BookingContext bookingContext) {
        BookableActionStripFragment f = new BookableActionStripFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOKING_CONTEXT, bookingContext);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookable_action_strip, container, false);

        containerId = container.getId();

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_BOOKING_CONTEXT)) {
            Log.e(TAG, "No BookingContext");
            return view;
        }


        BookingContext bookingContext = (BookingContext) args.get(ARG_BOOKING_CONTEXT);

        ActionStrip actionStrip = view.findViewById(R.id.action_container);
        actionStrip.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookableActionStripFragment.this.getContext(), AvailabilityActivity.class);
                intent.putExtra(AvailabilityActivity.ARG_PRODUCT, bookingContext.getProduct());
                startActivity(intent);
            }
        });


        // Set Price
        Price price = bookingContext.getPrice();
        String checkAvailability = getContext().getString(R.string.button_next);
        String startingAt = getContext().getString(R.string.label_starting_at);
        String localizedPrice = String.format(Locale.getDefault(), getContext().getString(R.string.label_price_per_person), price.getLocalizedDescription(TravelerUI.getPreferredCurrency()));

        actionStrip.setStripValues(checkAvailability, startingAt, localizedPrice);

        return view;
    }


    /*
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
    */
}
