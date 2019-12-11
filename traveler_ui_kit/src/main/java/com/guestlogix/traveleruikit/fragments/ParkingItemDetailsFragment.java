package com.guestlogix.traveleruikit.fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Location;
import com.guestlogix.travelercorekit.models.ParkingItem;
import com.guestlogix.travelercorekit.models.ParkingItemDetails;
import com.guestlogix.travelercorekit.models.Price;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;

import java.util.Date;
import java.util.List;

public class ParkingItemDetailsFragment extends Fragment
        implements OnMapReadyCallback {
    private static final String TAG = "BookingItemDetailsFragm";
    private static final String ARG_PARKING_ITEM = "ARG_PARKING_ITEM";
    private static final String ARG_PARKING_ITEM_DETAILS = "ARG_PARKING_ITEM_DETAILS";
    private static final int ZOOM_LEVEL = 15;

    //TODO replace heavy map fragment with a static image
    //private ImageView staticMapImageView;
    private ParkingItemDetails parkingItemDetails;
    private ParkingItem parkingItem;

    public static ParkingItemDetailsFragment newInstance(ParkingItem item, CatalogItemDetails details) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARKING_ITEM, item);
        args.putSerializable(ARG_PARKING_ITEM_DETAILS, details);
        ParkingItemDetailsFragment fragment = new ParkingItemDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_item_details, container, false);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_PARKING_ITEM_DETAILS)) {
            Log.e(TAG, "No ParkingItemDetails in arguments");
            return null;
        }

        if (!args.containsKey(ARG_PARKING_ITEM)) {
            Log.e(TAG, "No Parking Item in arguments");
            return null;
        }

        parkingItem = (ParkingItem) args.get(ARG_PARKING_ITEM);
        parkingItemDetails = (ParkingItemDetails) args.get(ARG_PARKING_ITEM_DETAILS);

        if (parkingItemDetails == null) {
            Log.e(TAG, "No CatalogItemDetails");
            return view;
        }

        setTitle(view);
        setPhone(view);
        setLocation(view);
        setDateRange(view);
        setDescription(view);
        setPrice(view);
        setMap(view);
        return view;
    }

    private void setTitle(View view) {
        TextView titleTextView = view.findViewById(R.id.textView_parking_details_title);
        titleTextView.setText(parkingItemDetails.getTitle());
    }

    private void setPhone(View view) {
        List<String> phoneList = parkingItemDetails.getContact().getPhones();
        if (phoneList.size() > 0) {
            ((TextView) view.findViewById(R.id.textView_parking_details_contact)).setText(phoneList.get(0));
        }
    }

    private void setLocation(View view) {
        List<Location> locations = parkingItemDetails.getLocations();
        if (locations.size() > 0) {
            ((TextView) view.findViewById(R.id.textView_parking_details_address)).setText(locations.get(0).getAddress());
        }
    }

    private void setDateRange(View view) {
        Date lowerDate = parkingItemDetails.getDateRange().getLower();
        Date upperDate = parkingItemDetails.getDateRange().getUpper();
        String dateRangeString = DateHelper.formatToMonthDayYearTime(lowerDate) +
                " – " +
                DateHelper.formatToMonthDayYearTime(upperDate);
        ((TextView) view.findViewById(R.id.textView_parking_details_hours)).setText(dateRangeString);
    }
    private void setDescription(View view){
        ((TextView) view.findViewById(R.id.textView_parking_details_information_description)).setText(parkingItemDetails.getDescription());
    }

    private void setPrice(View view) {
        ((TextView) view.findViewById(R.id.textView_parking_details_total_price))
                .setText(parkingItemDetails.getPrice().getLocalizedDescriptionInBaseCurrency());
        ((TextView) view.findViewById(R.id.textView_parking_details_pay_online))
                .setText(parkingItemDetails.getPriceToPayOnline().getLocalizedDescriptionInBaseCurrency());
        ((TextView) view.findViewById(R.id.textView_parking_details_balance))
                .setText(parkingItemDetails.getPriceToPayOnsite().getLocalizedDescriptionInBaseCurrency());

        int payOnsiteComponentsVisibility = parkingItemDetails.getPriceToPayOnsite().getValueInBaseCurrency() < 0.01D ? View.GONE : View.VISIBLE;
        view.findViewById(R.id.textView_parking_details_divider).setVisibility(payOnsiteComponentsVisibility);
        view.findViewById(R.id.textView_parking_details_pay_online_label).setVisibility(payOnsiteComponentsVisibility);
        view.findViewById(R.id.textView_parking_details_pay_online).setVisibility(payOnsiteComponentsVisibility);
        view.findViewById(R.id.textView_parking_details_balance_label).setVisibility(payOnsiteComponentsVisibility);
        view.findViewById(R.id.textView_parking_details_balance).setVisibility(payOnsiteComponentsVisibility);
        view.findViewById(R.id.relativeLayout_parking_details_balance_desc).setVisibility(payOnsiteComponentsVisibility);
    }

    private void setMap(View view) {
        //TODO replace heavy map fragment with a static image
        FragmentManager childFragmentManager = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) childFragmentManager.findFragmentById(R.id.parking_details_map);
        Assertion.eval(mapFragment != null);
        mapFragment.getMapAsync(this);

        ((TextView) view.findViewById(R.id.textView_parking_details_information_description)).setText(parkingItemDetails.getDescription());
        LinearLayout infoLinearLayout = view.findViewById(R.id.linearLayout_parking_details_information);
        View hoursDistanceDivider = view.findViewById(R.id.textView_parking_details_information_divider);
        TextView hoursTextView = view.findViewById(R.id.textView_parking_details_information_hours);
        TextView hoursLabelTextView = view.findViewById(R.id.textView_parking_details_information_hours_label);
        TextView distanceTextView = view.findViewById(R.id.textView_parking_details_information_distance);
        TextView distanceLabelTextView = view.findViewById(R.id.textView_parking_details_information_distance_label);

        boolean hasDistance = false;
        boolean hasHours = false;
        List<Attribute> attributes = parkingItemDetails.getInformation();
        for (Attribute attribute : attributes) {
            if (attribute.getLabel().equalsIgnoreCase("Hours of operation")) {
                hasHours = true;
                hoursTextView.setText(attribute.getValue());
            } else if (attribute.getLabel().equalsIgnoreCase("Distance from the terminal")) {
                hasDistance = true;
                distanceTextView.setText(attribute.getValue());
            } else {
                // inflating views is lighter-weight than using a recyclerView/adapter.
                LinearLayout attributeLinearLayout = (LinearLayout) LayoutInflater.from(getContext())
                        .inflate(R.layout.item_attribute, infoLinearLayout, false);
                ((TextView) attributeLinearLayout.findViewById(R.id.itemLabel)).setText(attribute.getLabel());
                TextView valueTextView = attributeLinearLayout.findViewById(R.id.itemValue);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    valueTextView.setText(Html.fromHtml(attribute.getValue(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    valueTextView.setText(Html.fromHtml(attribute.getValue()));
                }
                infoLinearLayout.addView(attributeLinearLayout);
            }
        }

        if (!hasHours) {
            hoursTextView.setVisibility(View.GONE);
            hoursLabelTextView.setVisibility(View.GONE);
            hoursDistanceDivider.setVisibility(View.GONE);
        }
        if (!hasDistance) {
            distanceTextView.setVisibility(View.GONE);
            distanceLabelTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(
                parkingItemDetails.getGeolocation().getLatitude(),
                parkingItemDetails.getGeolocation().getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        //todo icon as per design .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerBitmap("$" + price, false)));
        googleMap.addMarker(markerOptions);
    }
}
