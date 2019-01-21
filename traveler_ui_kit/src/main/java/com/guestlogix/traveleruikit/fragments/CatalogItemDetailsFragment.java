package com.guestlogix.traveleruikit.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.viewmodels.StatefulViewModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogItemDetailsFragment extends Fragment {
    private View mView;

    private ViewPager catalogItemDetailsPager;
    private TabLayout catalogItemDetailsTabs;
    private CatalogItem catalogItem;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextView startingAtValueTextView;
    private Button checkAvailabilityButton;
    private TextInputEditText dateEditText;
    private TextInputEditText timeEditText;
    private ProgressBar detailsProgressbar;
    private ProgressBar checkAvailabilityProgressBar;
    private RelativeLayout itemDetailsLayout;

    CatalogItemDetailsViewModel catalogItemDetailsViewModel;

    public CatalogItemDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_catalog_item_details, container, false);
        setUp(mView);
        setupListeners();
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


        catalogItemDetailsViewModel.getMyCalendarObservable().observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                updateDateLabel();
                updateTimeLabel();
            }
        });

        catalogItemDetailsViewModel.getCatalogItemDetailsObservable().observe(this, new Observer<CatalogItemDetails>() {
            @Override
            public void onChanged(CatalogItemDetails catalogItemDetails) {
                updateView(catalogItemDetails);
            }
        });

        catalogItemDetailsViewModel.getStatus().observe(this, this::onStateChange);
        catalogItemDetailsViewModel.getAvailabilityStatus().observe(this, this::onAvailabilityStateChange);

        catalogItemDetailsViewModel.updateCatalog(catalogItem);
    }

    View.OnClickListener checkAvailabilityOnClickListener = v -> {
        catalogItemDetailsViewModel.checkAvailability();
    };

    private void setUp(View view) {

        titleTextView = view.findViewById(R.id.titleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        imageView = view.findViewById(R.id.imageView);
        startingAtValueTextView = view.findViewById(R.id.startingAtValueTextView);
        checkAvailabilityButton = view.findViewById(R.id.checkAvailabilityButton);
        catalogItemDetailsPager = view.findViewById(R.id.catalogItemPager);
        catalogItemDetailsTabs = view.findViewById(R.id.catalogItemTabs);
        dateEditText = view.findViewById(R.id.dateEditText);
        timeEditText = view.findViewById(R.id.timeEditText);
        detailsProgressbar = view.findViewById(R.id.itemDetailsProgressBar);
        itemDetailsLayout = view.findViewById(R.id.itemDetailsLayout);
        checkAvailabilityProgressBar = view.findViewById(R.id.checkAvailabilityProgressBar);
    }

    private void setupListeners() {
        checkAvailabilityButton.setOnClickListener(checkAvailabilityOnClickListener);
        dateEditText.setOnFocusChangeListener(this::datePickerFocusHandler);
        timeEditText.setOnFocusChangeListener(this::timePickerFocusHandler);

    }

    private void updateView(CatalogItemDetails catalogItemDetails) {
        titleTextView.setText(catalogItemDetails.getTitle());
        startingAtValueTextView.setText(String.format(Locale.CANADA, "$%f/per person", catalogItemDetails.getPriceStartingAt()));

        try {
            if (null != catalogItemDetails.getImageURL() && catalogItemDetails.getImageURL().size() > 0) {
                Traveler.loadImage(new URL(catalogItemDetails.getImageURL().get(0)), imageView);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
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

        Calendar myCalendar = catalogItemDetailsViewModel.getMyCalendar();
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        catalogItemDetailsViewModel.setMyCalendar(myCalendar);
    };

    TimePickerDialog.OnTimeSetListener timePickerListener = (view, hourOfDay, minute) -> {

        Calendar myCalendar = catalogItemDetailsViewModel.getMyCalendar();
        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);

        catalogItemDetailsViewModel.setMyCalendar(myCalendar);
    };

    private void updateDateLabel() {
        dateEditText.setText(DateHelper.getPrettyDateAsString(catalogItemDetailsViewModel.getMyCalendar().getTime()));
    }

    private void updateTimeLabel() {
        timeEditText.setText(DateHelper.getTimeAsString(catalogItemDetailsViewModel.getMyCalendar().getTime()));
    }

    private void datePickerFocusHandler(View view, boolean focus) {
        if (!focus) {
            String date = ((TextView) view).getText().toString();
        } else {
            Calendar myCalendar = catalogItemDetailsViewModel.getMyCalendar();
            new DatePickerDialog(getActivity(), datePickerListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private void timePickerFocusHandler(View view, boolean focus) {
        if (!focus) {
            String date = ((TextView) view).getText().toString();

        } else {
            Calendar myCalendar = catalogItemDetailsViewModel.getMyCalendar();
            new TimePickerDialog(getActivity(), timePickerListener, myCalendar
                    .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
        }
    }

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

    private void onAvailabilityStateChange(CatalogItemDetailsViewModel.CheckAvailabilityState state) {
        if (state == CatalogItemDetailsViewModel.CheckAvailabilityState.LOADING) {
            checkAvailabilityProgressBar.setVisibility(View.VISIBLE);
            checkAvailabilityButton.setVisibility(View.GONE);
        } else {
            checkAvailabilityProgressBar.setVisibility(View.GONE);
            checkAvailabilityButton.setVisibility(View.VISIBLE);

            switch (state) {
                case AVAILABLE:
                    // TODO
                    break;
                case NOT_AVAILABLE:
                    // TODO
                    break;
                case ERROR:
                    onCheckAvailabilityError();
                    break;
            }
        }
    }

    private void onCheckAvailabilityError() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.unexpected_error))
                .setMessage(getString(R.string.unknown_error_message))
                .create();

        dialog.show();
    }
}
