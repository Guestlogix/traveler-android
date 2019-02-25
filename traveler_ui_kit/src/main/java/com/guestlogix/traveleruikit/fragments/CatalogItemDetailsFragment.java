package com.guestlogix.traveleruikit.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.traveleruikit.adapters.TimeSlotSpinnerAdapter;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;
import com.guestlogix.traveleruikit.widgets.WrapContentViewPager;
import com.guestlogix.traveleruikit.widgets.DatePickerCell;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogItemDetailsFragment extends BaseFragment {

    private static final String ARG_CATALOG_ITEM = "catalog_item";

    private View mView;
    private NestedScrollView mainNestedScrollView;
    private WrapContentViewPager catalogItemDetailsPager;
    private TabLayout catalogItemDetailsTabs;
    private CatalogItem catalogItem;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextInputEditText timeEditText;
    private TextInputLayout timeTextInputLayout;
    private Spinner timeSlotsSpinner;
    private RelativeLayout timeRelativeLayout;

    private ActionStrip actionStrip;
    private DatePickerCell datePickerCell;

    private CatalogItemDetailsViewModel catalogItemDetailsViewModel;

    public CatalogItemDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_catalog_item_details, container, false);
        mainNestedScrollView = mView.findViewById(R.id.mainNestedScrollView);
        titleTextView = mView.findViewById(R.id.titleTextView);
        descriptionTextView = mView.findViewById(R.id.descriptionTextView);
        imageView = mView.findViewById(R.id.imageView);
        catalogItemDetailsPager = mView.findViewById(R.id.catalogItemPager);
        catalogItemDetailsTabs = mView.findViewById(R.id.catalogItemTabs);
        timeTextInputLayout = mView.findViewById(R.id.timeTextInputLayout);
        timeSlotsSpinner = mView.findViewById(R.id.timeSlotsSpinner);
        timeRelativeLayout = mView.findViewById(R.id.timeRelativeLayout);

        actionStrip = mView.findViewById(R.id.action_container);
        datePickerCell = mView.findViewById(R.id.datePickerCell);

//        timeEditText.setOnClickListener(this::timePickerOnclick);
//        checkAvailabilityButton.setOnClickListener(this::checkAvailabilityOnClick);
        timeSlotsSpinner.setOnItemSelectedListener(timeSlotOnItemSelectedListener);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catalogItemDetailsViewModel = ViewModelProviders.of(getActivityContext()).get(CatalogItemDetailsViewModel.class);
        catalogItemDetailsViewModel.getCatalogItemDetailsObservable().observe(this, this::setView);
        catalogItemDetailsViewModel.getAvailableTimeSlotsObservable().observe(this, this::onTimeSlotsChanged);
        catalogItemDetailsViewModel.getSelectedDateObservable().observe(this, this::onSelectedDateChanged);
//        catalogItemDetailsViewModel.getSelectedTimeObservable().observe(this, this::onSelectedTimeChanged);
    }

    private void setView(CatalogItemDetails catalogItemDetails) {
        titleTextView.setText(catalogItemDetails.getTitle());

        if (null != catalogItemDetails.getImageURL() && catalogItemDetails.getImageURL().size() > 0) {
            //TODO Load Image: Traveler.loadImage(new URL(catalogItemDetails.getImageURL().get(0)), imageView);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionTextView.setText(Html.fromHtml(catalogItemDetails.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descriptionTextView.setText(Html.fromHtml(catalogItemDetails.getDescription()));
        }

        ItemInformationTabsPagerAdapter adapter = new ItemInformationTabsPagerAdapter(getActivityContext().getSupportFragmentManager(), getActivityContext());
        adapter.setContactInfo(catalogItemDetails.getContact());
        adapter.setInformationList(catalogItemDetails.getInformation());
        adapter.setLocationsList(catalogItemDetails.getLocations());
        catalogItemDetailsPager.setAdapter(adapter);
        catalogItemDetailsPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                catalogItemDetailsPager.requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter.notifyDataSetChanged();

        catalogItemDetailsTabs.setupWithViewPager(catalogItemDetailsPager);
    }

    DatePickerDialog.OnDateSetListener datePickerListener = (view, year, monthOfYear, dayOfMonth) -> {
        Calendar date = catalogItemDetailsViewModel.getSelectedDate();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        catalogItemDetailsViewModel.setSelectedDate(date);
    };

    private void setTimeLabel() {
        String selectedTime = DateHelper.formatTime(catalogItemDetailsViewModel.getSelectedTime());
        if (null != selectedTime && !selectedTime.isEmpty()) {
            timeEditText.setText(selectedTime);
        } else {
            timeEditText.setText("");
            timeEditText.setHint(getString(R.string.hint_select_time));
        }
    }

    private void timePickerOnclick(View view) {
        timeEditText.setError(null);
        timeSlotsSpinner.performClick();
    }

    private void checkAvailabilityOnClick(View view) {
        boolean isFormComplete = true;

        if (catalogItemDetailsViewModel.getSelectedDate() == null) {
//            dateEditText.setError(getString(R.string.hint_select_time));
//            focusOnView(dateTextInputLayout);
            isFormComplete = false;
        }

        if (catalogItemDetailsViewModel.getTimeRequired()) {
            if (catalogItemDetailsViewModel.getSelectedTime() == null) {
                timeEditText.setError(getString(R.string.hint_select_time));
                focusOnView(timeRelativeLayout);
                isFormComplete = false;
            }
        }

        if (isFormComplete) {
            catalogItemDetailsViewModel.requestBooking();
        }
    }

    AdapterView.OnItemSelectedListener timeSlotOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            catalogItemDetailsViewModel.setSelectedTime(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void onSelectedTimeChanged(Long selectedTime) {
        setTimeLabel();
    }

    private void onSelectedDateChanged(Calendar selectedDate) {
        datePickerCell.setDate(selectedDate);
        timeRelativeLayout.setVisibility(View.GONE);
        catalogItemDetailsViewModel.checkAvailability();
    }

//    private void onAvailabilityStateChange(CatalogItemDetailsViewModel.CheckAvailabilityState state) {
//        if (state == CatalogItemDetailsViewModel.CheckAvailabilityState.LOADING) {
//            checkAvailabilityProgressBar.setVisibility(View.VISIBLE);
//            checkAvailabilityButton.setVisibility(View.INVISIBLE);
//        } else {
//            checkAvailabilityProgressBar.setVisibility(View.GONE);
//            checkAvailabilityButton.setVisibility(View.VISIBLE);
//
//            switch (state) {
//                case AVAILABLE:
//                    checkAvailabilityButton.setEnabled(true);
//                    dateEditText.setError(null);
//                    break;
//                case NOT_AVAILABLE:
//                    checkAvailabilityButton.setEnabled(false);
//                    dateEditText.setError(getString(R.string.not_available));
//                    break;
//                case ERROR:
//                    onCheckAvailabilityError();
//                    break;
//            }
//        }
//    }

    private void onTimeSlotsChanged(List<Long> availableTimeSlots) {
        if (availableTimeSlots.size() > 0) {
            timeRelativeLayout.setVisibility(View.VISIBLE);
        }
        TimeSlotSpinnerAdapter timeSlotSpinnerAdapter = new TimeSlotSpinnerAdapter(getActivityContext(), android.R.layout.simple_spinner_dropdown_item, availableTimeSlots);
        timeSlotsSpinner.setAdapter(timeSlotSpinnerAdapter);
        timeSlotsSpinner.setOnItemSelectedListener(timeSlotOnItemSelectedListener);
    }

    private void onCheckAvailabilityError() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivityContext())
                .setTitle(getString(R.string.unexpected_error))
                .setMessage(getString(R.string.unknown_error_message))
                .create();

        dialog.show();
    }

    private void focusOnView(View view) {
        mainNestedScrollView.post(() -> mainNestedScrollView.smoothScrollTo(0, view.getBottom() + 50));
    }
}
