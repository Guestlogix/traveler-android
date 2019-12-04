package com.guestlogix.traveleruikit.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guestlogix.travelercorekit.callbacks.ParkingSearchCallback;
import com.guestlogix.travelercorekit.models.BoundingBox;
import com.guestlogix.travelercorekit.models.Coordinate;
import com.guestlogix.travelercorekit.models.ParkingItem;
import com.guestlogix.travelercorekit.models.ParkingItemQuery;
import com.guestlogix.travelercorekit.models.ParkingItemSearchResult;
import com.guestlogix.travelercorekit.models.QueryItem;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.ParkingSearchResultAdapter;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

import java.util.ArrayList;
import java.util.List;

public class ParkingActivity extends AppCompatActivity implements
        ParkingSearchCallback,
        RetryFragment.InteractionListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        ParkingSearchResultAdapter.OnParkingSearchItemClickListener {
    public static final String TAG = "ParkingActivity";
    public static final String ARG_PARKING_QUERY = "parkingQuery";
    private static final int PAGE_SIZE = 10;
    private static final int MARKER_MAX_FONT_SIZE = 18;
    private static final float MARKER_CENTER_X_DIVISOR = 2f;
    private static final float MARKER_CENTER_Y_DIVISOR = 2.5f;
    private static final int SCROLL_SLOWDOWN_FACTOR = 2;
    private static final int MAP_ANIMATION_DURATION_MS = 500;

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private ProgressBar progressBar;
    private RecyclerView parkingSearchRecyclerView;
    private RecyclerView.SmoothScroller smoothScroller;
    private LinearLayoutManager linearLayoutManager;

    private ParkingSearchResultAdapter parkingSearchResultAdapter;
    private List<Marker> markerList = new ArrayList<>();
    private ParkingItemQuery previousSearchQuery;
    private FragmentTransactionQueue transactionQueue;
    @Nullable private Marker selectedMarker = null;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        //QueryItem holds the initial search query
        QueryItem initialQueryItem = (QueryItem) getIntent().getSerializableExtra(ARG_PARKING_QUERY);
        Assertion.eval(initialQueryItem != null);
        setTitle(initialQueryItem.getTitle());

        ActionBar actionBar = getSupportActionBar();
        Assertion.eval(actionBar != null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Assertion.eval(mapFragment != null);
        mapFragment.getMapAsync(this);

        progressBar = findViewById(R.id.spinner);
        parkingSearchRecyclerView = findViewById(R.id.parking_items_recyclerview);
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO alvtag check button state
                LatLngBounds visibleLatLngBounds = map.getProjection().getVisibleRegion().latLngBounds;
                ParkingItemQuery newSearchQuery = new ParkingItemQuery(previousSearchQuery.getAirportIATA(),
                        previousSearchQuery.getDateRange(),
                        latLngBoundsToBoundingBox(visibleLatLngBounds),
                        0, PAGE_SIZE);
                loadNewParkingItems(newSearchQuery);
            }
        });


        loadNewParkingItems((ParkingItemQuery) initialQueryItem.getSearchQuery());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        showMapFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof RetryFragment) {
            ((RetryFragment) fragment).setInteractionListener(this);
        }
    }

    @Override
    public void onRetry() {
        loadNewParkingItems(previousSearchQuery);
    }

    @Override
    public void onParkingSearchError(Error error) {
        Fragment fragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.booking_item_details_container, fragment);
        transactionQueue.addTransaction(transaction);
        error.printStackTrace();
    }

    @Override
    public void onParkingSearchSuccess(ParkingItemSearchResult searchResult) {
        LatLngBounds latLngBounds = boundingBoxToLatLngBounds(searchResult.getQuery().getBoundingBox());

        showMapFragment();
        progressBar.setVisibility(View.INVISIBLE);

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0), MAP_ANIMATION_DURATION_MS, null);
        map.clear();
        setMapMarkers(searchResult);
        if (parkingSearchResultAdapter == null) {
            parkingSearchResultAdapter = new ParkingSearchResultAdapter(searchResult, this);

            smoothScroller = new LinearSmoothScroller(this) {
                @Override
                protected int getHorizontalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;
                }

                @Override
                protected int calculateTimeForScrolling(int dx) {
                    return SCROLL_SLOWDOWN_FACTOR * super.calculateTimeForScrolling(dx);
                }
            };
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            parkingSearchRecyclerView.setLayoutManager(linearLayoutManager);
            parkingSearchRecyclerView.setAdapter(parkingSearchResultAdapter);
        } else {
            parkingSearchResultAdapter.setData(searchResult);
            parkingSearchResultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onParkingSearchItemClick(ParkingItem parkingItem) {
        for (Marker marker : markerList) {
            if (parkingItem == marker.getTag()) {
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), MAP_ANIMATION_DURATION_MS, null);
                clearSelectedMarker();
                setSelectedMarker(marker, parkingItem);
            }
        }
        int newIndex = parkingSearchResultAdapter.setSelectedParkingItem(parkingItem);
        scrollListToIndex(newIndex);
    }

    private void scrollListToIndex(int newIndex) {
        smoothScroller.setTargetPosition(newIndex);
        linearLayoutManager.startSmoothScroll(smoothScroller);
    }

    private void setMapMarkers(ParkingItemSearchResult searchResult) {
        for (ParkingItem parkingItem : searchResult.getItems()) {
            Coordinate coordinate = parkingItem.getCoordinate();
            LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());

            //TODO alvtag: this is oft used, refactor
            String price = String.valueOf((int) parkingItem.getPrice().getValueInBaseCurrency());

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerBitmap("$" + price, false)));

            Marker marker = map.addMarker(markerOptions);
            marker.setTag(parkingItem);
            markerList.add(marker);
        }
    }

    /**
     * @return true if the listener has consumed the event (i.e., the default behavior should not occur);
     * false otherwise (i.e., the default behavior should occur). The default behavior is
     * for the camera to move to the marker and an info window to appear.
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        ParkingItem parkingItem = (ParkingItem) marker.getTag();
        if (parkingItem == null) {
            Log.e(TAG, "onMarkerClick has null parkingItem!");
            return false;
        }

        clearSelectedMarker();
        setSelectedMarker(marker, parkingItem);
        return false;
    }

    private void showMapFragment() {
        View view = mapFragment.getView();
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void loadNewParkingItems(ParkingItemQuery query) {
        previousSearchQuery = query;
        progressBar.setVisibility(View.VISIBLE);
        //todo Alvtag set button state to loading

        ParkingItemQuery parkingItemQuery = new ParkingItemQuery(query.getAirportIATA(),
                query.getDateRange(), query.getBoundingBox(), 0, PAGE_SIZE);

        Traveler.searchParkingItems(parkingItemQuery, this);
    }

    private void setSelectedMarker(Marker marker, ParkingItem parkingItem) {
        parkingSearchResultAdapter.setSelectedParkingItem(parkingItem);
        scrollListToIndex(parkingSearchResultAdapter.getPositionForParkingItem(parkingItem));
        String price = String.valueOf((int) parkingItem.getPrice().getValueInBaseCurrency());
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerBitmap("$" + price, true)));
        selectedMarker = marker;
    }

    private void clearSelectedMarker() {
        if (selectedMarker != null) {
            ParkingItem selectedMarkerParkingItem = (ParkingItem) selectedMarker.getTag();
            if (selectedMarkerParkingItem != null) {
                String price = String.valueOf((int) selectedMarkerParkingItem.getPrice().getValueInBaseCurrency());
                selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerBitmap("$" + price, false)));
            }
        }
    }

    private Bitmap createCustomMarkerBitmap(String text, boolean isSelected) {
        @DrawableRes int drawableResource = isSelected ? R.drawable.ic_price_pin_selected : R.drawable.ic_price_pin;
        Bitmap immutableBitmap = BitmapFactory.decodeResource(getResources(), drawableResource);

        Bitmap mutableBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setTextSize(MARKER_MAX_FONT_SIZE * getResources().getDisplayMetrics().density);

        @ColorRes int colorRes = isSelected ? R.color.white : R.color.black;
        paint.setColor(ContextCompat.getColor(this, colorRes));
        paint.setTypeface(Typeface.create("Roboto", Typeface.NORMAL));

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        float x = ((float) (mutableBitmap.getWidth() - bounds.width())) / MARKER_CENTER_X_DIVISOR;
        float y = ((float) (mutableBitmap.getHeight() + bounds.height())) / MARKER_CENTER_Y_DIVISOR;
        canvas.drawText(text, x, y, paint);

        return mutableBitmap;
    }

    private static LatLngBounds boundingBoxToLatLngBounds(BoundingBox boundingBox) {
        Coordinate topLeftCoordinate = boundingBox.getTopLeftCoordinate();
        Coordinate bottomRightCoordinate = boundingBox.getBottomRightCoordinate();
        LatLng southWest = new LatLng(bottomRightCoordinate.getLatitude(), topLeftCoordinate.getLongitude());
        LatLng northEast = new LatLng(topLeftCoordinate.getLatitude(), bottomRightCoordinate.getLongitude());
        return new LatLngBounds(southWest, northEast);
    }

    private static BoundingBox latLngBoundsToBoundingBox(LatLngBounds latLngBounds) {
        LatLng southWest = latLngBounds.southwest;
        LatLng northEast = latLngBounds.northeast;
        Coordinate topLeftCoordinate = new Coordinate(northEast.latitude, southWest.longitude);
        Coordinate bottomRightCoordinate = new Coordinate(southWest.latitude, northEast.longitude);
        return new BoundingBox(topLeftCoordinate, bottomRightCoordinate);
    }
}
