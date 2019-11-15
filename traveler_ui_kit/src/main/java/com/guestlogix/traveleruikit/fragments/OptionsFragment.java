package com.guestlogix.traveleruikit.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.activities.PassSelectionActivity;
import com.guestlogix.traveleruikit.adapters.OptionsAdapter;
import com.guestlogix.traveleruikit.adapters.RadioAdapter;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.REQUEST_CODE_ORDER_FLOW;
import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.RESULT_OK_ORDER_CONFIRMED;

public class OptionsFragment extends Fragment implements RadioAdapter.Listener, FetchPassesCallback {
    public static String TAG = "OptionsFragment";
    public static String ARG_PRODUCT = "ARG_PRODUCT";
    public static String ARG_AVAILABILITY = "ARG_AVAILABILITY";

    private ActionStrip actionStrip;
    private BookingProduct bookingProduct;

    public static OptionsFragment getInstance(Product product, Availability availability) {
        OptionsFragment fragment = new OptionsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        args.putSerializable(ARG_AVAILABILITY, availability);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_AVAILABILITY) || !args.containsKey(ARG_PRODUCT)) {
            Log.e(TAG, "No Availability/Product");
            return view;
        }

        Availability availability = (Availability) args.get(ARG_AVAILABILITY);
        bookingProduct = (BookingProduct) args.get(ARG_PRODUCT);

        if (availability.getBookingOptionSet() == null) {
            Log.e(TAG, "Availability doesn't have a BookingOptionSet");
            return view;
        }

        OptionsAdapter adapter = new OptionsAdapter(getContext(), availability.getBookingOptionSet().getOptions());
        adapter.setListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_options);
        recyclerView.setAdapter(adapter);

        actionStrip = view.findViewById(R.id.actionStrip_optionsFragment);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        String localizedPrice = String.format(Locale.getDefault(), getContext().getString(R.string.label_price_per_person), bookingProduct.getPrice().getLocalizedDescription(TravelerUI.getPreferredCurrency()));
        actionStrip.setValue(localizedPrice);
        actionStrip.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStrip.changeState(ActionStrip.ActionStripState.LOADING);

                BookingOption bookingOption = availability.getBookingOptionSet().getOptions().get(adapter.getSelectedIndex());
                Traveler.fetchPasses(bookingProduct, availability, bookingOption, OptionsFragment.this);
            }
        });

        return view;
    }

    @Override
    public void onSelectedIndexChanged(int index) {
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
    }

    @Override
    public void onPassFetchError(Error error) {
        // TODO: Use the error object to display a better message

        new AlertDialog.Builder(getContext())
                .setMessage("Error fetching passes")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onPassFetchSuccess(List<Pass> pass) {
        // TODO: Use fragments for most of the buy flow

        Intent intent = new Intent(this.getContext(), PassSelectionActivity.class);
        intent.putExtra(PassSelectionActivity.EXTRA_PASSES, new ArrayList<>(pass));
        intent.putExtra(PassSelectionActivity.EXTRA_PRODUCT, bookingProduct);
        startActivityForResult(intent, REQUEST_CODE_ORDER_FLOW);
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
