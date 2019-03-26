package com.guestlogix.traveleruikit.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.BookableProductViewModel;
import com.guestlogix.traveleruikit.widgets.DatePickerCell;
import com.guestlogix.traveleruikit.widgets.ListPickerCell;

public class BookableInformationSelectionFragment extends BaseFragment {
    private ListPickerCell timePickerCell;
    private DatePickerCell datePickerCell;

    public BookableInformationSelectionFragment() {
        // Do nothing
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookable_information_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datePickerCell = view.findViewById(R.id.datePickerCell);
        datePickerCell.setHint(getString(R.string.hint_select_date));
        timePickerCell = view.findViewById(R.id.timePickerCell);
        timePickerCell.setHint(getString(R.string.hint_select_time));

        BookableProductViewModel sharedViewModel = ViewModelProviders.of(getActivityContext()).get(BookableProductViewModel.class);
        sharedViewModel.getObservableOptions().observe(getActivityContext(), timePickerCell::setValueList);
        sharedViewModel.getObservableOptionsTitle().observe(getActivityContext(), timePickerCell::setHint);
        sharedViewModel.getAvailabilityState().observe(this, this::onStateChanged);

        datePickerCell.setOnDateChangedListener(sharedViewModel::onDateChanged);
        timePickerCell.setOnItemSelectedListener(sharedViewModel::onOptionChanged);
    }

    private void onCheckAvailabilityError() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivityContext())
                .setTitle(getString(R.string.unexpected_error))
                .setMessage(getString(R.string.unknown_error_message))
                .create();

        dialog.show();
    }

    private void onStateChanged(BookableProductViewModel.State state) {
        switch (state) {
            case DEFAULT:
                timePickerCell.setVisibility(View.GONE);
                break;
            case NOT_AVAILABLE:
                timePickerCell.setVisibility(View.GONE);
                datePickerCell.setError(getString(R.string.not_available));
                break;
            case OPTION_NEEDED:
                timePickerCell.setVisibility(View.VISIBLE);
                break;
            case AVAILABLE:
                datePickerCell.setError(null);
                break;
            case ERROR:
                onCheckAvailabilityError();
                break;
        }
    }

}
