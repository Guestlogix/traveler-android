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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    private QueryItem queryItem;
    private FragmentTransactionQueue transactionQueue;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private ProgressBar progressBar;
    private RecyclerView parkingSearchRecyclerView;
    private ParkingSearchResultAdapter parkingSearchResultAdapter;

    private List<Marker> markerList = new ArrayList<>();

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

        queryItem = (QueryItem) getIntent().getSerializableExtra(ARG_PARKING_QUERY);
        Assertion.eval(queryItem != null);
        setTitle(queryItem.getTitle());

        ActionBar actionBar = getSupportActionBar();
        Assertion.eval(actionBar != null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Assertion.eval(mapFragment != null);
        mapFragment.getMapAsync(this);

        progressBar = findViewById(R.id.spinner);
        parkingSearchRecyclerView = findViewById(R.id.parking_items_recyclerview);

        reloadParkingItem();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        mapFragment.getView().setVisibility(View.INVISIBLE);
    }

    private void reloadParkingItem() {
        progressBar.setVisibility(View.VISIBLE);

        ParkingItemQuery query = (ParkingItemQuery) queryItem.getSearchQuery();  //TODO ALVTAG Do this cast/typecheck way earlier, maybe pass in
        ParkingItemQuery parkingItemQuery = new ParkingItemQuery(query.getAirportIATA(),
                query.getDateRange(), query.getBoundingBox(), 0, PAGE_SIZE);
        Traveler.searchParkingItems(parkingItemQuery, this);
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
        reloadParkingItem();
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

        mapFragment.getView().setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        map.clear();
        setMapMarkers(searchResult);
        if (parkingSearchResultAdapter == null) {
            parkingSearchResultAdapter = new ParkingSearchResultAdapter(searchResult, this);
            parkingSearchRecyclerView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            parkingSearchRecyclerView.setAdapter(parkingSearchResultAdapter);
        }
    }

    @Override
    public void onParkingSearchItemClick(ParkingItem parkingItem) {
        Log.d("ALVTAG", "parkingitem clicked:" + parkingItem.getTitle());
        parkingSearchResultAdapter.setSelectedParkingItem(parkingItem);
    }

    private void setMapMarkers(ParkingItemSearchResult searchResult) {
        for (ParkingItem parkingItem : searchResult.getItems()) {
            Coordinate coordinate = parkingItem.getCoordinate();
            LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());

            //TODO alvtag: this is oft used, refactor
            String price = String.valueOf((int) parkingItem.getPrice().getValueInBaseCurrency());

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerBitmap("$" + price)));

            markerList.add(map.addMarker(markerOptions));

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private Bitmap createCustomMarkerBitmap(String text) {
        Bitmap immutableBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_price_pin);

        Bitmap mutableBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setTextSize(MARKER_MAX_FONT_SIZE * getResources().getDisplayMetrics().density);

        paint.setColor(ContextCompat.getColor(this, R.color.black));
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
}
