package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.callbacks.BookingItemCategoriesSearchCallback;
import com.guestlogix.travelercorekit.models.BookingItemCategory;
import com.guestlogix.travelercorekit.models.BookingItemQuery;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;

import java.util.ArrayList;
import java.util.List;

/*
 FIXME
 Note: SDK UI layer is in the process of being refactored and it is Guestlogix' decision to shelve the UI layer of the SDK for the moment. As such, this view controller's implementation is neither complete nor correct. I should not be used in production.

 This comment is the same as Omar's in iOS PR
 */
@Deprecated
public class BookingSearchActivity extends AppCompatActivity implements BookingItemCategoriesSearchCallback {

    private static final String TAG = "BookingSearchActivity";

    AppCompatEditText etSearch;
    AppCompatEditText etLocation;
    RecyclerView rvCategories;
    CategoryAdapter categoryAdapter;

    public final static String KEY_ITEM_QUERY = "key_item_query";
    BookingItemQuery bookingItemQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booking_search);

        etSearch = findViewById(R.id.etSearch);
        etLocation = findViewById(R.id.etLocation);
        rvCategories = findViewById(R.id.rvCategories);

        findViewById(R.id.btnSearch).setOnClickListener(v -> showResultActivity());

        Object queryObject = getIntent().getSerializableExtra(KEY_ITEM_QUERY);
        if (queryObject != null) {
            bookingItemQuery = (BookingItemQuery) queryObject;
        }

        if (bookingItemQuery != null) {
            etSearch.setText(bookingItemQuery.getQueryText());
            etLocation.setText(bookingItemQuery.getCity());
        }


        //TODO figure out how to compose categories list for this screen.
        ArrayList<CategoryAdapter.CategoryItem> lstCategories = new ArrayList<>();
        if (bookingItemQuery.getCategories() != null && bookingItemQuery.getCategories().size() > 0) {
            for (BookingItemCategory category : bookingItemQuery.getCategories()) {
                lstCategories.add(new CategoryAdapter.CategoryItem("", category, false));
            }
            categoryAdapter.setCategories(lstCategories);
            categoryAdapter.notifyDataSetChanged();
        } else {
            fetchCategories();
        }


        categoryAdapter = new CategoryAdapter(BookingSearchActivity.this, new ArrayList<>());
        rvCategories.setAdapter(categoryAdapter);
        rvCategories.setLayoutManager(new LinearLayoutManager(BookingSearchActivity.this));
    }

    private void showResultActivity() {
        Intent intent = new Intent(BookingSearchActivity.this, BookingSearchResultActivity.class);
        intent.putExtra(BookingSearchResultActivity.KEY_SEARCH_TEXT, etSearch.getText().toString());
        intent.putExtra(BookingSearchResultActivity.KEY_City, etLocation.getText().toString());
        intent.putExtra(BookingSearchResultActivity.KEY_CATEGORIES, categoryAdapter.getSelectedCategories());
        startActivity(intent);
    }

    private void fetchCategories() {
        Traveler.fetchBookingItemCategories(this);
    }

    //region ===================== BookingItemCategoriesSearchCallback ======================

    @Override
    public void onCategoriesSearchSuccess(List<BookingItemCategory> categories) {
        ArrayList<CategoryAdapter.CategoryItem> categoryItems = new ArrayList<>();
        for (BookingItemCategory category : categories) {
            categoryItems.add(new CategoryAdapter.CategoryItem("", category, false));
        }
        categoryAdapter.setCategories(categoryItems);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoriesSearchError(Error error) {
        //TODO should we do something here right now? It's unclear at this moment, how this screen should behave with new dynamic categories
        Log.e(TAG, "Fetch dynamic categories error: " + error.getMessage());
    }


    //endregion
}
