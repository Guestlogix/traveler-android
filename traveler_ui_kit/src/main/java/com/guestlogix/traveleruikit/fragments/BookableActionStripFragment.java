package com.guestlogix.traveleruikit.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.guestlogix.travelercorekit.models.Price;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.activities.AvailabilityActivity;
import com.guestlogix.traveleruikit.models.BookingContext;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.util.Locale;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.REQUEST_CODE_ORDER_FLOW;
import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.RESULT_OK_ORDER_CONFIRMED;

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
                intent.putExtra(AvailabilityActivity.ARG_PRODUCT, bookingContext.getBookingItem());
                startActivityForResult(intent, REQUEST_CODE_ORDER_FLOW);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Activity activity = getActivity();
        if (requestCode == REQUEST_CODE_ORDER_FLOW && resultCode == RESULT_OK_ORDER_CONFIRMED && activity != null) {
            activity.setResult(RESULT_OK_ORDER_CONFIRMED);
            activity.finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
