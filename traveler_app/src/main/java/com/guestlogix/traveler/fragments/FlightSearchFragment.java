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
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.viewmodels.SearchFlightViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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

    private SearchFlightViewModel mViewModel;

    final Calendar myCalendar = Calendar.getInstance();

    final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("^([A-Z]{2}|[A-Z]\\d|\\d[A-Z])([1-9](\\d{1,3})?)$");

    public static FlightSearchFragment newInstance() {
        return new FlightSearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_flight_search, container, false);
        ButterKnife.bind(this, view);

        departureDateEditText.setShowSoftInputOnFocus(false);
        departureDateEditText.setOnClickListener(departureDateOnClickListener);
        searchFlightsButton.setOnClickListener(searchFlightOnClickListener);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchFlightViewModel.class);
        // TODO: Use the ViewModel
    }

    View.OnClickListener searchFlightOnClickListener = Navigation.createNavigateOnClickListener(R.id.flight_search_result_action, null);

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

}
