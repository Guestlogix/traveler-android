package com.guestlogix.traveleruikit.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.viewmodels.CatalogItemDetailsViewModel;

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

        catalogItemDetailsViewModel.getStatus().observe(this, new Observer<CatalogItemDetailsViewModel.State>() {
            @Override
            public void onChanged(CatalogItemDetailsViewModel.State status) {
                switch (status) {
                    case LOADING:
                        //TODO Handle Loading state
                        Toast.makeText(getActivity(), "Loading", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        //TODO Handle Success state
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        //TODO Handle Error state
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        catalogItemDetailsViewModel.updateCatalog(catalogItem);
    }

    View.OnClickListener checkAvailabilityOnClickListener = v -> {
        //TODO Integrate Check Availability state
        Toast.makeText(getActivity(), "Check Availability", Toast.LENGTH_SHORT).show();

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
    }

    private void setupListeners() {
        checkAvailabilityButton.setOnClickListener(checkAvailabilityOnClickListener);
        dateEditText.setOnFocusChangeListener(this::datePickerFocusHandler);
        timeEditText.setOnFocusChangeListener(this::timePickerFocusHandler);

    }

    private void updateView(CatalogItemDetails catalogItemDetails) {
        titleTextView.setText(catalogItemDetails.getTitle());
        startingAtValueTextView.setText(String.format(Locale.CANADA, "$%f/per person", catalogItemDetails.getPriceStartingAt()));

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
}
