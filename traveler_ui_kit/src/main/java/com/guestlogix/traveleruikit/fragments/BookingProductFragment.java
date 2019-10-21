package com.guestlogix.traveleruikit.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.guestlogix.travelercorekit.callbacks.FetchAvailabilitiesCallback;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.BookingOption;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.models.BookingContext;
import com.guestlogix.traveleruikit.models.PurchaseContext;
import com.guestlogix.traveleruikit.viewmodels.BookableProductViewModel;
import com.guestlogix.traveleruikit.widgets.DatePickerCell;
import com.guestlogix.traveleruikit.widgets.ListPickerCell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingProductFragment extends BaseFragment implements FetchAvailabilitiesCallback, BookingContext.BookingContextUpdateListener {
    private static final String ARG_PRODUCT = "bookable_product_arg";

    // Views
    private ListPickerCell flavourPicker;
    private DatePickerCell datePicker;

    // Data
    private BookingContext bookingContext;

    // Callbacks
    private BookingContextChangedListener bookingContextChangedListener;

    public BookingProductFragment() {
        // Do nothing
    }

    public static BookingProductFragment getInstance(BookingItem bookingItem) {
        BookingProductFragment f = new BookingProductFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, bookingItem);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args != null && args.containsKey(ARG_PRODUCT)) {
            BookingItem bookingItem = (BookingItem) args.getSerializable(ARG_PRODUCT);
            bookingContext = new BookingContext(bookingItem);
            bookingContext.setUpdateListener(this);

            if (this.bookingContextChangedListener != null) {
                this.bookingContextChangedListener.onBookingContextChanged(bookingContext);
            }
        }

        return inflater.inflate(R.layout.fragment_bookable_information_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //datePicker = view.findViewById(R.id.datePickerCell);
        datePicker.setHint(getString(R.string.hint_select_date));
        //flavourPicker = view.findViewById(R.id.timePickerCell);
        flavourPicker.setHint(getString(R.string.hint_select_time));
        flavourPicker.setVisibility(View.GONE);

        Calendar today = Calendar.getInstance();
        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 0, 0 ,0);
        datePicker.setMinDate(today);

        BookableProductViewModel sharedViewModel = ViewModelProviders.of(getActivityContext()).get(BookableProductViewModel.class);
        sharedViewModel.getObservableOptions().observe(getActivityContext(), flavourPicker::setValueList);
        sharedViewModel.getObservableOptionsTitle().observe(getActivityContext(), flavourPicker::setHint);

        datePicker.setOnDateChangedListener(this::onDateSelected);
        flavourPicker.setOnItemSelectedListener(this::onFlavourSelected);
    }

    private void onStateChanged(PurchaseContext.State state, BookingContext bookingContext) {
        switch (state) {
            case DEFAULT:
                flavourPicker.setVisibility(View.GONE);
                break;
            case NOT_AVAILABLE:
                flavourPicker.setVisibility(View.GONE);
                datePicker.setError(getString(R.string.not_available));
                break;
            case OPTION_REQUIRED:
                List<String> options = new ArrayList<>();

                for (BookingOption o : bookingContext.getOptions()) {
                    options.add(o.getValue());
                }

                flavourPicker.setValueList(options);
                flavourPicker.setHint(bookingContext.getOptionsTitle());
                flavourPicker.setVisibility(View.VISIBLE);
                break;
            case AVAILABLE:
                datePicker.setError(null);
                break;
        }
    }

    // Set current date and fetch data from api.
    private void onDateSelected(Calendar calendar) {
        bookingContext.setState(PurchaseContext.State.LOADING);
        Traveler.fetchAvailabilities((Product) bookingContext, calendar.getTime(), calendar.getTime(), this);
    }

    private void onFlavourSelected(int pos) {
        bookingContext.setOption(pos);
    }

    @Override
    public void onAvailabilitySuccess(List<Availability> availabilities) {
        bookingContext.setAvailabilities(availabilities);
    }

    @Override
    public void onAvailabilityError(Error error) {
        final AlertDialog dialog = new AlertDialog.Builder(getActivityContext())
                .setTitle(getString(R.string.unexpected_error))
                .setMessage(error.getMessage())
                .create();

        dialog.show();
    }

    @Override
    public void onBookingContextUpdate(BookingContext bookingContext) {
        onStateChanged(bookingContext.getState(), bookingContext);

        if (bookingContextChangedListener != null) {
            bookingContextChangedListener.onBookingContextChanged(bookingContext);
        }
    }

    public void setBookingContextChangedListener(BookingContextChangedListener bookingContextChangedListener) {
        this.bookingContextChangedListener = bookingContextChangedListener;
    }

    public interface BookingContextChangedListener {
        void onBookingContextChanged(BookingContext bookingContext);
    }
}
