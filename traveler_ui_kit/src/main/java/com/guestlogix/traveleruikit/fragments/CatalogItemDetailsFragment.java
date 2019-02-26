package com.guestlogix.traveleruikit.fragments;


import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;
import com.guestlogix.traveleruikit.widgets.WrapContentViewPager;
import com.guestlogix.traveleruikit.widgets.DatePickerCell;
import com.guestlogix.traveleruikit.widgets.ListPickerCell;


import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CatalogItemDetailsFragment extends BaseFragment {

    private NestedScrollView mainNestedScrollView;
    private WrapContentViewPager catalogItemDetailsPager;
    private TabLayout catalogItemDetailsTabs;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;

    private ActionStrip actionStrip;
    private DatePickerCell datePickerCell;
    private ListPickerCell timePickerCell;

    public CatalogItemDetailsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_catalog_item_details, container, false);

        mainNestedScrollView = mView.findViewById(R.id.mainNestedScrollView);
        titleTextView = mView.findViewById(R.id.titleTextView);
        descriptionTextView = mView.findViewById(R.id.descriptionTextView);
        imageView = mView.findViewById(R.id.imageView);
        catalogItemDetailsPager = mView.findViewById(R.id.catalogItemPager);
        catalogItemDetailsTabs = mView.findViewById(R.id.catalogItemTabs);

        actionStrip = mView.findViewById(R.id.action_container);
        datePickerCell = mView.findViewById(R.id.datePickerCell);
        timePickerCell = mView.findViewById(R.id.timePickerCell);

        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CatalogItemDetailsViewModel catalogItemDetailsViewModel = ViewModelProviders.of(getActivityContext()).get(CatalogItemDetailsViewModel.class);

        catalogItemDetailsViewModel.getObservableCatalogItemDetails().observe(this, this::setView);
        catalogItemDetailsViewModel.getObservableTimeSlots().observe(this, this::onTimesChanged);
        catalogItemDetailsViewModel.getObservableSelectedDate().observe(this, this::onDateChanged);
        catalogItemDetailsViewModel.getObservableActionState().observe(this, this::onActionState);

        actionStrip.setActionOnClickListener(catalogItemDetailsViewModel::onActionSubmit);
        datePickerCell.setOnDateChangedListener(catalogItemDetailsViewModel::setBookingDate);
        timePickerCell.setOnItemSelectedListener(catalogItemDetailsViewModel::setBookingTime);
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

        String checkAvailability = getString(R.string.button_check_availability);
        String startingAt = getString(R.string.label_starting_at);
        String price = String.format(Locale.getDefault(), "%s per person", catalogItemDetails.getPriceStartingAt().getFormattedValue());

        actionStrip.setStripValues(checkAvailability, startingAt, price);

        ItemInformationTabsPagerAdapter adapter =
                new ItemInformationTabsPagerAdapter(getActivityContext().getSupportFragmentManager(), getActivity());

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

    private void onDateChanged(Calendar selectedDate) {
        datePickerCell.setDate(selectedDate);
    }

    private void onTimesChanged(List<String> times) {
        if (times == null || times.isEmpty()) {
            timePickerCell.setVisibility(View.GONE);
        } else {
            timePickerCell.setVisibility(View.VISIBLE);
            timePickerCell.setValueList(times);
        }
    }

    // Translates VM state to strip state.
    private void onActionState(CatalogItemDetailsViewModel.ActionState state) {
        switch (state) {
            case LOADING:
                actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
                break;
            case NOT_AVAILABLE:
                actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
                datePickerCell.setError(getString(R.string.not_available));
                focusOnView(datePickerCell);
                break;
            case AVAILABLE:
                actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                break;
            case ERROR:
                onCheckAvailabilityError();
                actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
                break;
            case TIME_REQUIRED:
                timePickerCell.setError(getString(R.string.required));
                focusOnView(timePickerCell);
                actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
        }
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
