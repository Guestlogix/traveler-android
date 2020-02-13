package com.guestlogix.traveleruikit.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.guestlogix.travelercorekit.models.BoundingBox;
import com.guestlogix.travelercorekit.models.Coordinate;
import com.guestlogix.travelercorekit.models.ParkingItemQuery;
import com.guestlogix.travelercorekit.models.Range;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;

import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FindParkingActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "ParkingActivity";
    public static final String ARG_PARKING_QUERY = "parkingQuery";
    public static final String NEAR_ME = "near me";
    public static final String NEAR_AIRPORT = "near airport";
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;
    private static final double BOUNDING_BOX_RADIUS = 0.1d;

    private TextView findParkingToggleNearMeTextView;
    private TextView findParkingToggleNearAirportTextView;
    private TextView dropOffDateTextView;
    private TextView dropOffTimeTextView;
    private TextView pickupDateTextView;
    private TextView pickupTimeTextView;
    private LinearLayout findParkingLinearLayout;
    private EditText airportCodeEditText;

    private String tabChoice;
    private ParkingItemQuery searchQuery;
    private GoogleApiClient googleApiClient;
    private Location location;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            location = locationResult.getLastLocation();
            Log.d(TAG, "location set to (" + location.getLatitude() + "," + location.getLongitude() + ")");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_parking);

        //QueryItem holds the initial search query
        searchQuery = (ParkingItemQuery) getIntent().getSerializableExtra(ARG_PARKING_QUERY);
        Assertion.eval(searchQuery != null);
        setTitle(getString(R.string.find_parking));

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        findParkingLinearLayout = findViewById(R.id.linearLayout_findParking_airport_code);
        airportCodeEditText = findViewById(R.id.editText_findParking_airport_code);

        findParkingToggleNearMeTextView = findViewById(R.id.textView_findParking_toggle_nearMe);
        findParkingToggleNearMeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateLocationPermission();
                tabChoice = NEAR_ME;
                findParkingLinearLayout.setVisibility(View.GONE);
                setNearMeView();
            }
        });
        findParkingToggleNearAirportTextView = findViewById(R.id.textView_findParking_toggle_nearAirport);
        findParkingToggleNearAirportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabChoice = NEAR_AIRPORT;
                findParkingLinearLayout.setVisibility(View.VISIBLE);
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


        if (!TextUtils.isEmpty(searchQuery.getAirportIATA())) {
            // if we have airport code, show user the airport tab
            findParkingToggleNearAirportTextView.performClick();
        } else {
            //otherwise we're looking at "near me", check for location.
            tabChoice = NEAR_ME;
            if (!isPlayServicesAvailable()) {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Google Play Services Not Available")
                        .create()
                        .show();
            } else {
                initiateLocationPermission();
            }
        }

        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();

        setNearMeView();
    }

    private void initiateLocationPermission() {
        // if we already have a location, we already have the permission.
        if (location != null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionsToRequest = {Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
            requestPermissions(permissionsToRequest, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (!hasLocationPermissionsGranted() &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {

                new AlertDialog.Builder(this).
                        setMessage("This permission is needed to get your location.").
                        setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String[] permissionsToRequest = {Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
                                requestPermissions(permissionsToRequest, LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();

            } else if (googleApiClient != null) {
                googleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!hasLocationPermissionsGranted()) {
            return;
        }

        Task<Location> locationTask = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        if (locationTask != null && locationTask.isComplete() && locationTask.getResult() != null) {
            location = locationTask.getResult();
            Log.d(TAG, "location set to (" + location.getLatitude() + "," + location.getLongitude() + ")");
        } else {
            // only start loc updates if location was null
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed due to: " + connectionResult.getErrorMessage());
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (!hasLocationPermissionsGranted()) {
            initiateLocationPermission();
        } else {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }

    private boolean hasLocationPermissionsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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

        String airportCode = null;
        BoundingBox boundingBox = null;
        if (NEAR_ME.equals(tabChoice) && location != null) {
            double lattitude = location.getLatitude();
            double longitude = location.getLongitude();
            Coordinate topLeft = new Coordinate(lattitude + BOUNDING_BOX_RADIUS, longitude - BOUNDING_BOX_RADIUS);
            Coordinate bottomRight = new Coordinate(lattitude - BOUNDING_BOX_RADIUS, longitude + BOUNDING_BOX_RADIUS);
            boundingBox = new BoundingBox(topLeft, bottomRight);
        } else if (NEAR_AIRPORT.equals(tabChoice)) {
            airportCode = airportCodeEditText.getText().toString();
        }

        ParkingItemQuery newQuery = new ParkingItemQuery(
                airportCode,
                new Range<>(lower, upper),
                boundingBox,
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
        findParkingToggleNearMeTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_find_parking_left_selected));
        ViewCompat.setBackgroundTintList(findParkingToggleNearMeTextView, ContextCompat.getColorStateList(this, R.color.colorPrimary));

        findParkingToggleNearAirportTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        findParkingToggleNearAirportTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_find_parking_right_unselected));
        ViewCompat.setBackgroundTintList(findParkingToggleNearAirportTextView, ContextCompat.getColorStateList(this, R.color.off_white));
    }

    private void setNearAirportView() {
        findParkingToggleNearMeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        findParkingToggleNearMeTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_find_parking_left_unselected));
        ViewCompat.setBackgroundTintList(findParkingToggleNearMeTextView, ContextCompat.getColorStateList(this, R.color.off_white));

        findParkingToggleNearAirportTextView.setTextColor(ContextCompat.getColor(this, R.color.off_white));
        findParkingToggleNearAirportTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_find_parking_right_selected));
        ViewCompat.setBackgroundTintList(findParkingToggleNearAirportTextView, ContextCompat.getColorStateList(this, R.color.colorPrimary));
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
            Calendar calendar = Calendar.getInstance();
            if (hourOfDay < 12) {
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

    private boolean isPlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }
}
