package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.BookingItemCategory;
import com.guestlogix.travelercorekit.models.BookingItemQuery;
import com.guestlogix.traveleruikit.R;

import java.util.ArrayList;
import java.util.EnumSet;

public class BookingSearchActivity extends AppCompatActivity {

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


        ArrayList<CategoryAdapter.CategoryItem> lstCategories = new ArrayList<>();
        for (BookingItemCategory bookingItemCategory : EnumSet.allOf(BookingItemCategory.class)) {
            lstCategories.add(new CategoryAdapter.CategoryItem("", bookingItemCategory, bookingItemQuery != null && bookingItemQuery.getCategories().contains(bookingItemCategory)));
        }

        categoryAdapter = new CategoryAdapter(BookingSearchActivity.this, lstCategories);
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
}
