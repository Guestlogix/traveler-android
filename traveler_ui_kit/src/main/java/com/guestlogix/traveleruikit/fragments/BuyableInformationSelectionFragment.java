package com.guestlogix.traveleruikit.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.BookableProductViewModel;
import com.guestlogix.traveleruikit.widgets.DatePickerCell;
import com.guestlogix.traveleruikit.widgets.ListPickerCell;

/**
 * TODO: This class is not implemented yet. Need Purchase context
 */
public class BuyableInformationSelectionFragment extends BaseFragment {

    public BuyableInformationSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookable_information_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatePickerCell datePickerCell = view.findViewById(R.id.datePickerCell);
        datePickerCell.setHint(getString(R.string.hint_select_date));
        ListPickerCell timePickerCell = view.findViewById(R.id.timePickerCell);
        timePickerCell.setHint(getString(R.string.hint_select_time));

        BookableProductViewModel sharedViewModel = ViewModelProviders.of(getActivityContext()).get(BookableProductViewModel.class);
        sharedViewModel.getBookingTimes().observe(this, timePickerCell::setValueList);
        sharedViewModel.getAvailabilityState().observe(this, (state -> {
            switch (state) {
                case DEFAULT:
                    timePickerCell.setVisibility(View.GONE);
                    break;
                case NOT_AVAILABLE:
                    timePickerCell.setVisibility(View.GONE);
                    datePickerCell.setError(getString(R.string.not_available));
                    break;
                case TIME_REQUIRED:
                    timePickerCell.setVisibility(View.VISIBLE);
                    //datePickerCell.setError(null);
                    break;
                case AVAILABLE:
                    datePickerCell.setError(null);
                    break;
                case ERROR:
                    onCheckAvailabilityError();
                    break;
            }
        }));

        datePickerCell.setOnDateChangedListener(sharedViewModel::onDateChanged);
        timePickerCell.setOnItemSelectedListener(sharedViewModel::onTimeChanged);
    }

    private void onCheckAvailabilityError() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivityContext())
                .setTitle(getString(R.string.unexpected_error))
                .setMessage(getString(R.string.unknown_error_message))
                .create();

        dialog.show();
    }

}
