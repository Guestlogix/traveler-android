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

/**
 * TODO: This class is not implemented yet. Need Purchase context
 */
public class BuyableInformationSelectionFragment extends BaseFragment {

    public BuyableInformationSelectionFragment() {
        // Do nothing.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookable_information_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO
    }

    private void onCheckAvailabilityError() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivityContext())
                .setTitle(getString(R.string.unexpected_error))
                .setMessage(getString(R.string.unknown_error_message))
                .create();

        dialog.show();
    }

}
