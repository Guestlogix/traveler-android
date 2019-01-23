package com.guestlogix.traveler.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.viewmodels.FlightSearchViewModel;
import com.guestlogix.travelercorekit.utilities.DateHelper;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlightSearchFragment extends Fragment {

    TextView flightNumberEditText;
    TextView departureDateEditText;
    TextView searchFlightsButton;
    View mView;

    private FlightSearchViewModel mViewModel;

    final Calendar myCalendar = Calendar.getInstance();
    final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("^([A-Z]{2}|[A-Z]\\d|\\d[A-Z])([1-9][0-9]{0,3}|[0-9]{0,3}[1-9])$");

    public static FlightSearchFragment newInstance() {
        return new FlightSearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_flight_search, container, false);

        setupView(mView);
        setupListeners();

        return mView;
    }

    private void setupListeners() {
        flightNumberEditText.setOnFocusChangeListener(this::flightNumberFocusHandler);
        departureDateEditText.setOnFocusChangeListener(this::departureDateFocusHandler);
        departureDateEditText.setOnEditorActionListener(this::softInputSubmit);
        searchFlightsButton.setOnClickListener(this::navigateToFlightSearchResults);
    }

    private void setupView(View view) {
        flightNumberEditText = view.findViewById(R.id.flightNumberEditText);
        departureDateEditText = view.findViewById(R.id.departureDateEditText);
        searchFlightsButton = view.findViewById(R.id.searchFlightsButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(FlightSearchViewModel.class);
    }

    private void navigateToFlightSearchResults(View view) {
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

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    private void updateLabel() {
        departureDateEditText.setText(DateHelper.getPrettyDateAsString(myCalendar.getTime()));
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

    private void flightNumberFocusHandler(View view, boolean focus) {
        if (!focus) {
            String flightNumber = ((TextView) view).getText().toString();
            validateFlightNumber(flightNumber);
        }
    }

    private void departureDateFocusHandler(View view, boolean focus) {
        if (!focus) {
            String date = ((TextView) view).getText().toString();
            validateFlightDate(date);
        } else {
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private boolean softInputSubmit(View v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            searchFlightsButton.performClick();
            return true;
        }
        return false;
    }
}
