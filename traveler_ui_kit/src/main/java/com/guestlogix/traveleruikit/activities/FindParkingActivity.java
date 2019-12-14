package com.guestlogix.traveleruikit.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.guestlogix.travelercorekit.models.ParkingItemQuery;
import com.guestlogix.travelercorekit.models.Range;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;

import java.util.Calendar;
import java.util.Date;

public class FindParkingActivity extends AppCompatActivity {
    public static final String TAG = "ParkingActivity";
    public static final String ARG_PARKING_QUERY = "parkingQuery";

    private TextView findParkingToggleNearMeTextView;
    private TextView findParkingToggleNearAirportTextView;
    private TextView dropOffDateTextView;
    private TextView dropOffTimeTextView;
    private TextView pickupDateTextView;
    private TextView pickupTimeTextView;

    private ParkingItemQuery searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_parking);

        //QueryItem holds the initial search query
        searchQuery = (ParkingItemQuery) getIntent().getSerializableExtra(ARG_PARKING_QUERY);
        Assertion.eval(searchQuery != null);
        setTitle(getString(R.string.find_parking));

        ActionBar actionBar = getSupportActionBar();
        Assertion.eval(actionBar != null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        findParkingToggleNearMeTextView = findViewById(R.id.linearLayout_findParking_toggle_nearMe);
        findParkingToggleNearMeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNearMeView();
            }
        });
        findParkingToggleNearAirportTextView = findViewById(R.id.linearLayout_findParking_toggle_nearAirport);
        findParkingToggleNearAirportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNearAirportView();
            }
        });

        dropOffDateTextView = findViewById(R.id.textView_findParking_dropoff_date);
        dropOffDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropoffDatePicker(dropOffDateTextView.getText().toString());
            }
        });
        dropOffTimeTextView = findViewById(R.id.textView_findParking_dropoff_time);
        dropOffTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropoffTimePicker(dropOffTimeTextView.getText().toString());
            }
        });
        pickupDateTextView = findViewById(R.id.textView_findParking_pickup_date);
        pickupDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickupDatePicker(pickupDateTextView.getText().toString());
            }
        });
        pickupTimeTextView = findViewById(R.id.textView_findParking_pickup_time);
        pickupTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickupTimePicker(pickupTimeTextView.getText().toString());
            }
        });
        findViewById(R.id.button_find_parking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewQueryResult();
            }
        });
        setQueryDateTimes();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void setNewQueryResult() {
        Calendar lowerDate = stringToDate(dropOffDateTextView.getText().toString());
        Calendar lowerTime = stringToHourMinute(dropOffTimeTextView.getText().toString());
        Date lower = mergeDateTimeCalendars(lowerDate, lowerTime).getTime();

        Calendar upperDate = stringToDate(pickupDateTextView.getText().toString());
        Calendar upperTime = stringToHourMinute(pickupTimeTextView.getText().toString());
        Date upper = mergeDateTimeCalendars(upperDate, upperTime).getTime();

        ParkingItemQuery newQuery = new ParkingItemQuery(
                "YYZ",//TODO ALVTG
                new Range<>(lower, upper),
                null,
                0,
                ParkingActivity.PAGE_SIZE);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ARG_PARKING_QUERY, newQuery);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private Calendar mergeDateTimeCalendars(Calendar date, Calendar time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, date.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, time.get(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        calendar.set(Calendar.MINUTE, time.get(Calendar.SECOND));
        calendar.set(Calendar.AM_PM, time.get(Calendar.AM_PM));
        return calendar;
    }

    private void showDropoffDatePicker(String dateString) {
        Calendar calendar = stringToDate(dateString);
        new DatePickerDialog(this, new DateSetListener(dropOffDateTextView),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void showDropoffTimePicker(String timeString) {
        Calendar calendar = stringToHourMinute(timeString);
        int hour = calendar.get(Calendar.HOUR);
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            hour += 12;
        }

        TimePickerDialog dialog = new TimePickerDialog(this,
                new TimeSetListener(dropOffTimeTextView), hour,
                calendar.get(Calendar.MINUTE), false);
        dialog.show();
    }

    private void showPickupDatePicker(String dateString) {
        Calendar calendar = stringToDate(dateString);
        new DatePickerDialog(this, new DateSetListener(pickupDateTextView),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void showPickupTimePicker(String timeString) {
        Calendar calendar = stringToHourMinute(timeString);
        int hour = calendar.get(Calendar.HOUR);
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            hour += 12;
        }
        TimePickerDialog dialog = new TimePickerDialog(this,
                new TimeSetListener(pickupTimeTextView), hour,
                calendar.get(Calendar.MINUTE), false);
        dialog.show();
    }

    private void setQueryDateTimes() {
        dropOffDateTextView.setText(DateHelper.formatToMonthDayYearSlashDelimited(searchQuery.getDateRange().getLower()));
        dropOffTimeTextView.setText(DateHelper.formatToHourMinuteMeridian(searchQuery.getDateRange().getLower()));
        pickupDateTextView.setText(DateHelper.formatToMonthDayYearSlashDelimited(searchQuery.getDateRange().getUpper()));
        pickupTimeTextView.setText(DateHelper.formatToHourMinuteMeridian(searchQuery.getDateRange().getUpper()));
    }

    private void setNearMeView() {
        findParkingToggleNearMeTextView.setTextColor(ContextCompat.getColor(this, R.color.off_white));
        findParkingToggleNearMeTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_parking_button_left_selected));

        findParkingToggleNearAirportTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        findParkingToggleNearAirportTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_parking_button_right_unselected));
    }

    private void setNearAirportView() {
        findParkingToggleNearMeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        findParkingToggleNearMeTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_parking_button_left_unselected));

        findParkingToggleNearAirportTextView.setTextColor(ContextCompat.getColor(this, R.color.off_white));
        findParkingToggleNearAirportTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_parking_button_right_selected));
    }

    private static class DateSetListener implements DatePickerDialog.OnDateSetListener {
        TextView textViewToUpdate;

        private DateSetListener(TextView textViewToUpdate) {
            this.textViewToUpdate = textViewToUpdate;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            textViewToUpdate.setText(DateHelper.formatToMonthDayYearSlashDelimited(calendar.getTime()));
        }
    }

    private static class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
        TextView textViewToUpdate;

        private TimeSetListener(TextView textViewToUpdate) {
            this.textViewToUpdate = textViewToUpdate;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();if (hourOfDay < 12) {
                calendar.set(Calendar.AM_PM, Calendar.AM);
            } else {
                calendar.set(Calendar.AM_PM, Calendar.PM);
                hourOfDay -= 12;
            }
            calendar.set(Calendar.HOUR, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            textViewToUpdate.setText(DateHelper.formatToHourMinuteMeridian(calendar.getTime()));
        }
    }

    private static Calendar stringToHourMinute(String timeString) {
        String[] time = timeString.split(" ");
        String amPm = time[1];
        String[] hourMinute = time[0].split(":");
        int hour = Integer.valueOf(hourMinute[0]);
        int minute = Integer.valueOf(hourMinute[1]);
        Calendar calendar = Calendar.getInstance();
        if (amPm.equalsIgnoreCase("AM")) {
            calendar.set(Calendar.AM_PM, Calendar.AM);
        } else if (amPm.equalsIgnoreCase("PM")) {
            calendar.set(Calendar.AM_PM, Calendar.PM);
        }
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    private static Calendar stringToDate(String dateString) {
        String[] date = dateString.split("/");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(date[2]));
        calendar.set(Calendar.MONTH, Integer.valueOf(date[0]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date[1]));
        return calendar;
    }
}
