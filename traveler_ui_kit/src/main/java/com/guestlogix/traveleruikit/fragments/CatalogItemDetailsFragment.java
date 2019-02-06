package com.guestlogix.traveleruikit.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.guestlogix.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.viewmodels.StatefulViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogItemDetailsFragment extends Fragment {
    private View mView;
    private NestedScrollView mainNestedScrollView;
    private ViewPager catalogItemDetailsPager;
    private TabLayout catalogItemDetailsTabs;
    private CatalogItem catalogItem;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextView startingAtValueTextView;
    private Button checkAvailabilityButton;
    private TextInputEditText dateEditText;
    private TextInputLayout dateTextInputLayout;
    private TextInputEditText timeEditText;
    private TextInputLayout timeTextInputLayout;
    private Spinner timeSlotsSpinner;
    private ProgressBar detailsProgressbar;
    private ProgressBar checkAvailabilityProgressBar;
    private RelativeLayout itemDetailsLayout;
    private RelativeLayout timeRelativeLayout;

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
        startingAtValueTextView = mView.findViewById(R.id.startingAtValueTextView);
        checkAvailabilityButton = mView.findViewById(R.id.checkAvailabilityButton);
        catalogItemDetailsPager = mView.findViewById(R.id.catalogItemPager);
        catalogItemDetailsTabs = mView.findViewById(R.id.catalogItemTabs);
        dateEditText = mView.findViewById(R.id.dateEditText);
        dateTextInputLayout = mView.findViewById(R.id.dateTextInputLayout);
        timeEditText = mView.findViewById(R.id.timeEditText);
        timeTextInputLayout = mView.findViewById(R.id.timeTextInputLayout);
        timeSlotsSpinner = mView.findViewById(R.id.timeSlotsSpinner);
        detailsProgressbar = mView.findViewById(R.id.itemDetailsProgressBar);
        itemDetailsLayout = mView.findViewById(R.id.itemDetailsLayout);
        checkAvailabilityProgressBar = mView.findViewById(R.id.checkAvailabilityProgressBar);
        timeRelativeLayout = mView.findViewById(R.id.timeRelativeLayout);

        timeEditText.setOnClickListener(this::timePickerOnclick);
        dateEditText.setOnClickListener(this::datePickerOnclick);
        checkAvailabilityButton.setOnClickListener(this::checkAvailabilityOnClick);
        timeSlotsSpinner.setOnItemSelectedListener(timeSlotOnItemSelectedListener);

        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (null != bundle) {
            catalogItem = (CatalogItem) bundle.getSerializable("catalog_item");
        } else {
            //TODO throw runtime exception, fragment needs catalog item to show details
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catalogItemDetailsViewModel = ViewModelProviders.of(this).get(CatalogItemDetailsViewModel.class);
        catalogItemDetailsViewModel.setCatalogItem(catalogItem);
        catalogItemDetailsViewModel.getCatalogItemDetailsObservable().observe(this, catalogItemDetails -> setView(catalogItemDetails));
        catalogItemDetailsViewModel.getStatus().observe(this, this::onStateChange);
        catalogItemDetailsViewModel.getAvailabilityStatus().observe(this, this::onAvailabilityStateChange);
        catalogItemDetailsViewModel.getAvailableTimeSlotsObservable().observe(this, this::onTimeSlotsChanged);
        catalogItemDetailsViewModel.getSelectedDateObservable().observe(this, this::onSelectedDateChanged);
        catalogItemDetailsViewModel.getSelectedTimeObservable().observe(this, this::onSelectedTimeChanged);
        catalogItemDetailsViewModel.getTimeRequiredObservable().observe(this, this::onTimeRequiredChanged);

        catalogItemDetailsViewModel.updateCatalog(catalogItem);
    }

    private void setView(CatalogItemDetails catalogItemDetails) {
        titleTextView.setText(catalogItemDetails.getTitle());
        startingAtValueTextView.setText(String.format(Locale.CANADA, "%f %s/per person", catalogItemDetails.getPriceStartingAt().getValue(), catalogItemDetails.getPriceStartingAt().getCurrency()));

        if (null != catalogItemDetails.getImageURL() && catalogItemDetails.getImageURL().size() > 0) {
            //Traveler.loadImage(new URL(catalogItemDetails.getImageURL().get(0)), imageView);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionTextView.setText(Html.fromHtml(catalogItemDetails.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descriptionTextView.setText(Html.fromHtml(catalogItemDetails.getDescription()));
        }

        ItemInformationTabsPagerAdapter adapter = new ItemInformationTabsPagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        adapter.setContactInfo(catalogItemDetails.getContact());
        adapter.setInformationList(catalogItemDetails.getInformation());
        adapter.setLocationsList(catalogItemDetails.getLocations());
        catalogItemDetailsPager.setAdapter(adapter);
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

    private void setDateLabel() {
        String selectedDate = DateHelper.getPrettyDateAsString(catalogItemDetailsViewModel.getSelectedDate().getTime());
        if (null != selectedDate && !selectedDate.isEmpty()) {
            dateEditText.setText(selectedDate);
        } else {
            dateEditText.setText("");
            dateEditText.setHint(getString(R.string.hint_select_date));
        }
    }

    private void setTimeLabel() {
        String selectedTime = DateHelper.getDayMinutesAsTimeString(catalogItemDetailsViewModel.getSelectedTime());
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

    private void datePickerOnclick(View view) {
        dateEditText.setError(null);
        Calendar myCalendar = catalogItemDetailsViewModel.getSelectedDate();
        new DatePickerDialog(getActivity(), datePickerListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void checkAvailabilityOnClick(View view) {
        boolean isFormComplete = true;

        if (catalogItemDetailsViewModel.getSelectedDate() == null) {
            dateEditText.setError(getString(R.string.hint_select_time));
            focusOnView(dateTextInputLayout);
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
            //TODO Fetch Pass for selected product
        }
    }

    AdapterView.OnItemSelectedListener timeSlotOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            catalogItemDetailsViewModel.setSelectedTime(position);
            Log.v("CatalogItemDetails", String.format("Selected %d", position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.v("CatalogItemDetails", String.format("Selected none"));
        }
    };

    private void onStateChange(StatefulViewModel.State state) {
        switch (state) {
            case LOADING:
                detailsProgressbar.setVisibility(View.VISIBLE);
                itemDetailsLayout.setVisibility(View.GONE);
                break;
            case SUCCESS:
                detailsProgressbar.setVisibility(View.GONE);
                itemDetailsLayout.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                onErrorState();
                break;
        }
    }

    private void onErrorState() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.unexpected_error))
                .setMessage(getString(R.string.unknown_error_message))
                .setNeutralButton(getString(R.string.ok), ((dial, which) -> {
                    FragmentManager fm = getFragmentManager();
                    assert fm != null;
                    fm.popBackStack();

                    dial.dismiss();
                }))
                .setCancelable(false)
                .create();

        dialog.show();
    }

    private void onSelectedTimeChanged(Long selectedTime) {
        setTimeLabel();
    }

    private void onSelectedDateChanged(Calendar selectedDate) {
        setDateLabel();
        timeRelativeLayout.setVisibility(View.GONE);
        catalogItemDetailsViewModel.checkAvailability();
    }

    private void onTimeRequiredChanged(Boolean timeRequired) {
//        if (timeRequired) {
//            timeTextInputLayout.setVisibility(View.GONE);
//            timeSlotsSpinner.setVisibility(View.INVISIBLE);
//        } else {
//            timeTextInputLayout.setVisibility(View.VISIBLE);
//            timeSlotsSpinner.setVisibility(View.VISIBLE);
//        }
    }

    private void onAvailabilityStateChange(CatalogItemDetailsViewModel.CheckAvailabilityState state) {
        if (state == CatalogItemDetailsViewModel.CheckAvailabilityState.LOADING) {
            checkAvailabilityProgressBar.setVisibility(View.VISIBLE);
            checkAvailabilityButton.setVisibility(View.INVISIBLE);
        } else {
            checkAvailabilityProgressBar.setVisibility(View.GONE);
            checkAvailabilityButton.setVisibility(View.VISIBLE);

            switch (state) {
                case AVAILABLE:
                    checkAvailabilityButton.setEnabled(true);
                    dateEditText.setError(null);
                    break;
                case NOT_AVAILABLE:
                    checkAvailabilityButton.setEnabled(false);
                    dateEditText.setError(getString(R.string.not_available));
                    break;
                case ERROR:
                    onCheckAvailabilityError();
                    break;
            }
        }
    }

    private void onTimeSlotsChanged(List<Long> availableTimeSlots) {
        if (availableTimeSlots.size() > 0) {
            timeRelativeLayout.setVisibility(View.VISIBLE);
        }
        TimeSlotSpinnerAdapter timeSlotSpinnerAdapter = new TimeSlotSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, availableTimeSlots);
        timeSlotsSpinner.setAdapter(timeSlotSpinnerAdapter);
        timeSlotsSpinner.setOnItemSelectedListener(timeSlotOnItemSelectedListener);

    }

    private void onCheckAvailabilityError() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.unexpected_error))
                .setMessage(getString(R.string.unknown_error_message))
                .create();

        dialog.show();
    }

    private void focusOnView(View view) {
        mainNestedScrollView.post(() -> mainNestedScrollView.smoothScrollTo(0, view.getBottom() + 50));
    }
}
