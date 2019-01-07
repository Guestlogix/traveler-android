package com.guestlogix.traveler.fragments;

import android.app.DatePickerDialog;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.viewmodels.FlightSearchViewModel;
import com.guestlogix.travelercorekit.utilities.DateHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static androidx.navigation.Navigation.findNavController;

public class FlightSearchFragment extends Fragment {

    @BindView(R.id.flightNumberEditText)
    TextView flightNumberEditText;

    @BindView(R.id.departureDateEditText)
    TextView departureDateEditText;

    @BindView(R.id.departureDateTextInputLayout)
    TextInputLayout departureDateTextInputLayout;

    @BindView(R.id.searchFlightsButton)
    TextView searchFlightsButton;

    private FlightSearchViewModel mViewModel;

    final Calendar myCalendar = Calendar.getInstance();

    final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("^([A-Z]{2}|[A-Z]\\d|\\d[A-Z])([1-9](\\d{1,3})?)$");

    public static FlightSearchFragment newInstance() {
        return new FlightSearchFragment();
    }

    View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_flight_search, container, false);
        ButterKnife.bind(this, mView);

        departureDateEditText.setShowSoftInputOnFocus(false);
        departureDateEditText.setOnClickListener(departureDateOnClickListener);

        flightNumberEditText.setOnFocusChangeListener((view, focus) -> {
            if (!focus) {
                String flightNumber = ((TextView) view).getText().toString();

                validateFlightNumber(flightNumber);
            }
        });

        departureDateEditText.setOnFocusChangeListener((view, focus) -> {
            if (!focus) {
                String date = ((TextView) view).getText().toString();

                validateFlightDate(date);
            }
        });
        searchFlightsButton.setOnClickListener(searchFlightOnClickListener);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FlightSearchViewModel.class);
        // TODO: Use the ViewModel
    }

    View.OnClickListener searchFlightOnClickListener = v -> navigateToFlightSearchResults();

    private void navigateToFlightSearchResults() {
        String flightNumber = flightNumberEditText.getText().toString();
        String departureDate = DateHelper.getDateAsString(myCalendar.getTime());

        if (isFlightNumberValid(flightNumber) && !departureDate.isEmpty()) {
            FlightSearchFragmentDirections.FlightSearchResultAction directions = FlightSearchFragmentDirections.flightSearchResultAction(departureDate, flightNumber);
            Navigation.findNavController(mView).navigate(directions);
        } else {
            validateFlightNumber(flightNumber);
            validateFlightDate(departureDate);
        }
    }


    View.OnClickListener departureDateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    private void updateLabel() {
        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        departureDateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private Boolean isFlightNumberValid(String flightNumber) {
        Matcher m = FLIGHT_NUMBER_PATTERN.matcher(flightNumber);
        return m.find();
    }

    private void validateFlightNumber(String s) {
        if (null == s || !isFlightNumberValid(s)) {
            flightNumberEditText.setError(getString(R.string.flight_search_error));
        }
    }

    private void validateFlightDate(String date) {
        if (null == date || date.isEmpty()) {
            departureDateEditText.setError(getString(R.string.empty_date_error));
        }
    }

}
