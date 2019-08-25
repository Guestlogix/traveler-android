package com.guestlogix.traveleruikit.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.guestlogix.travelercorekit.callbacks.FetchAvailabilitiesCallback;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.activities.PassSelectionActivity;
import com.guestlogix.traveleruikit.calendarpicker.CalendarPicker;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.util.*;

public class AvailabilityFragment extends Fragment
        implements FetchAvailabilitiesCallback, CalendarPicker.CalendarPickerListener, FetchPassesCallback {
    public static String ARG_PRODUCT = "ARG_PRODUCT";
    static String TAG = "AvailabilityFragment";

    private Product product;
    private CalendarPicker picker;
    private ProgressBar progressBar;
    private Map<Long, Availability> availabilities = new HashMap<>();
    private ActionStrip actionStrip;
    private Availability selectedAvailability;
    private int containerId;

    public static AvailabilityFragment getInstance(Product product) {
        AvailabilityFragment fragment = new AvailabilityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_availability, container, false);

        containerId = container.getId();

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_PRODUCT)) {
            Log.e(TAG, "No Product");
            return view;
        }

        product = (Product) args.getSerializable(ARG_PRODUCT);

        actionStrip = view.findViewById(R.id.actionStrip_availabilityFragment);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        String localizedPrice = String.format(Locale.getDefault(), getContext().getString(R.string.label_price_per_person), product.getPrice().getLocalizedDescription(TravelerUI.getPreferredCurrency()));
        actionStrip.setValue(localizedPrice);
        actionStrip.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedAvailability.getBookingOptionSet() == null) {
                    actionStrip.changeState(ActionStrip.ActionStripState.LOADING);

                    Traveler.fetchPasses(product, selectedAvailability, null, AvailabilityFragment.this);


                } else {
                    Fragment fragment = OptionsFragment.getInstance(product, selectedAvailability);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(containerId, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }


            }
        });

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        progressBar = view.findViewById(R.id.progress_availability);

        picker = view.findViewById(R.id.calendarPicker_availability);
        picker.setMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        picker.setListener(this);

        onMonthChange(picker.getMonth(), picker.getYear());

        return view;
    }

    @Override
    public void onMonthChange(int month, int year) {
        // Disable UI + show progress

        picker.setAlpha(0.3f);
        picker.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);

        // Fetch availabilities

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        Date startDate = calendar.getTime();

        calendar.set(year, month + 1, 0);

        Date endDate = calendar.getTime();

        Traveler.fetchAvailabilities(product, startDate, endDate, this);
    }

    @Override
    public boolean isDateAvailable(Date date) {
        return availabilities.containsKey(getDaysSinceEpoch(date));
    }

    @Override
    public void onDateSelect(Date date) {
        Availability availability = availabilities.get(getDaysSinceEpoch(date));

        if (availability == null) {
            Log.e(TAG, "Could not find the right Availability");
            return;
        }

        this.selectedAvailability = availability;
        this.actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
    }

    @Override
    public void onAvailabilitySuccess(List<Availability> availabilities) {
        this.availabilities = new HashMap<>();

        for (Availability availability : availabilities) {
            this.availabilities.put(getDaysSinceEpoch(availability.getDate()), availability);
        }

        picker.reloadDates();
        picker.setAlpha(1);
        picker.setEnabled(true);

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAvailabilityError(Error error) {
        // TODO: handle this case

        picker.setAlpha(1);
        picker.setEnabled(true);

        progressBar.setVisibility(View.INVISIBLE);

        // TODO: Use the error object to display a better message

        new AlertDialog.Builder(getContext())
                .setMessage("Error fetching availabilities")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onMonthChange(picker.getMonth(), picker.getYear());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onPassFetchSuccess(List<Pass> pass) {
        // TODO: Use fragments for the rest of the flow
        Intent intent = new Intent(AvailabilityFragment.this.getContext(), PassSelectionActivity.class);
        intent.putExtra(PassSelectionActivity.EXTRA_PASSES, new ArrayList<>(pass));
        intent.putExtra(PassSelectionActivity.EXTRA_PRODUCT, product);
        startActivity(intent);
    }

    @Override
    public void onPassFetchError(Error error) {
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);

        // TODO: Use the error object to display a better message

        new AlertDialog.Builder(getContext())
                .setMessage("Error fetching passes")
                .setNeutralButton("OK", null)
                .create()
                .show();
    }

    static long getDaysSinceEpoch(Date date) {
       return date.getTime() / 86400000;
    }
}
