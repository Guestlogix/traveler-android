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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.traveleruikit.widgets.DatePickerCell;
import com.guestlogix.traveleruikit.widgets.ListPickerCell;
import com.guestlogix.traveleruikit.widgets.WrapContentViewPager;

import java.util.Calendar;
import java.util.List;

public class CatalogItemDetailsFragment extends BaseFragment {

    private NestedScrollView mainNestedScrollView;
    private WrapContentViewPager catalogItemDetailsPager;
    private TabLayout catalogItemDetailsTabs;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;

    private DatePickerCell datePickerCell;
    private ListPickerCell timePickerCell;

    public CatalogItemDetailsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_item_details, container, false);

        mainNestedScrollView = view.findViewById(R.id.mainNestedScrollView);
        titleTextView = view.findViewById(R.id.titleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        imageView = view.findViewById(R.id.imageView);
        catalogItemDetailsPager = view.findViewById(R.id.catalogItemPager);
        catalogItemDetailsTabs = view.findViewById(R.id.catalogItemTabs);

        datePickerCell = view.findViewById(R.id.datePickerCell);
        timePickerCell = view.findViewById(R.id.timePickerCell);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CatalogItemDetailsViewModel catalogItemDetailsViewModel = ViewModelProviders.of(getActivityContext()).get(CatalogItemDetailsViewModel.class);

        catalogItemDetailsViewModel.getObservableCatalogItemDetails().observe(this, this::setView);
        catalogItemDetailsViewModel.getObservableTimeSlots().observe(this, this::onTimesChanged);
        catalogItemDetailsViewModel.getObservableSelectedDate().observe(this, this::onDateChanged);
        catalogItemDetailsViewModel.getObservableActionState().observe(this, this::onActionState);

        datePickerCell.setOnDateChangedListener(catalogItemDetailsViewModel::setBookingDate);
        timePickerCell.setOnItemSelectedListener(catalogItemDetailsViewModel::setBookingTime);

        FragmentTransaction fragmentTransaction = getActivityContext().getSupportFragmentManager().beginTransaction();
        ActionStripContainerFragment actionStripContainerFragment = new ActionStripContainerFragment();

        fragmentTransaction.replace(R.id.action_container, actionStripContainerFragment);
        fragmentTransaction.commit();
    }

    private void setView(CatalogItemDetails catalogItemDetails) {
        titleTextView.setText(catalogItemDetails.getTitle());

        if (null != catalogItemDetails.getImageURL() && catalogItemDetails.getImageURL().size() > 0) {
            AssetManager.getInstance().loadImage(catalogItemDetails.getImageURL().get(0), imageView::setImageBitmap);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionTextView.setText(Html.fromHtml(catalogItemDetails.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descriptionTextView.setText(Html.fromHtml(catalogItemDetails.getDescription()));
        }

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
                break;
            case NOT_AVAILABLE:
                datePickerCell.setError(getString(R.string.not_available));
                focusOnView(datePickerCell);
                break;
            case AVAILABLE:
                break;
            case ERROR:
                onCheckAvailabilityError();
                break;
            case TIME_REQUIRED:
                timePickerCell.setError(getString(R.string.required));
                focusOnView(timePickerCell);
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
