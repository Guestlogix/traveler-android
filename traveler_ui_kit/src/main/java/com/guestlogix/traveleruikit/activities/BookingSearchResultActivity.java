package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.BookingSearchCallback;
import com.guestlogix.travelercorekit.models.BookingItemCategory;
import com.guestlogix.travelercorekit.models.BookingItemQuery;
import com.guestlogix.travelercorekit.models.BookingItemSearchResult;
import com.guestlogix.travelercorekit.models.BookingItemSort;
import com.guestlogix.travelercorekit.models.BookingItemSortField;
import com.guestlogix.travelercorekit.models.BookingItemSortOrder;
import com.guestlogix.travelercorekit.models.PriceRangeFilter;
import com.guestlogix.travelercorekit.models.Range;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.fragments.BookingSearchResultFragment;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class BookingSearchResultActivity extends AppCompatActivity
        implements BookingSearchCallback, RetryFragment.InteractionListener, SortAndFilterDialog.SortAndFilterDialogCallback {

    public static String KEY_SEARCH_TEXT = "searchTextKey";
    public static String KEY_City = "searchCityTextKey";
    public static String KEY_CATEGORIES = "categoriesKey";
    public static String KEY_Default_SORT_ITEM = "sortKey";

    private FragmentTransactionQueue transactionQueue;
    private String searchText;
    private String searchCity;
    private ArrayList<BookingItemCategory> searchCategories;
    private BookingItemSort currentBookingItemSort = new BookingItemSort(BookingItemSortField.SORT_BY_PRICE, BookingItemSortOrder.LOW_TO_HIGH);
    private BookingItemSort initialBookingItemSort = new BookingItemSort(BookingItemSortField.SORT_BY_PRICE, BookingItemSortOrder.LOW_TO_HIGH);
    private PriceRangeFilter currentPriceRangeFilter;
    private PriceRangeFilter initialPriceRangeFilter;

    LinearLayout llSortAndFilters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booking_search_result);

        searchText = getIntent().getStringExtra(KEY_SEARCH_TEXT);
        searchCity = getIntent().getStringExtra(KEY_City);

        Serializable serializableCategories = getIntent().getSerializableExtra(KEY_CATEGORIES);
        if (serializableCategories != null) {
            if (serializableCategories instanceof ArrayList) {
                searchCategories = (ArrayList<BookingItemCategory>) serializableCategories;
            } else {
                throw new ClassCastException("categories must be of ArrayList<BookingItemCategory> type");
            }
        }

        Serializable serializableSort = getIntent().getSerializableExtra(KEY_Default_SORT_ITEM);
        if (serializableSort != null) {
            if (serializableSort instanceof BookingItemSort) {
                currentBookingItemSort = (BookingItemSort) serializableSort;
            } else {
                throw new ClassCastException("sort object must be of BookingItemSort type");
            }
        }

        Serializable serializablePriceRange = getIntent().getSerializableExtra(KEY_Default_SORT_ITEM);
        if (serializablePriceRange != null) {
            if (serializablePriceRange instanceof PriceRangeFilter) {
                currentPriceRangeFilter = (PriceRangeFilter) serializablePriceRange;
            } else {
                throw new ClassCastException("price range object must be of PriceRangeFilter type");
            }
        }

        llSortAndFilters = findViewById(R.id.llSortAndFilters);
        llSortAndFilters.setVisibility(View.GONE);
        llSortAndFilters.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SortAndFilterDialog(BookingSearchResultActivity.this,
                                currentPriceRangeFilter, initialPriceRangeFilter,
                                currentBookingItemSort, initialBookingItemSort,
                                BookingSearchResultActivity.this).show();
                    }
                });

        findViewById(R.id.tvChangeSearch).setOnClickListener(v -> finish());

        ((TextView) findViewById(R.id.tvCity)).setText((searchCity == null || searchCity.isEmpty()) ? "Anywhere" : searchCity);

        TextView tvSearchText = findViewById(R.id.tvSearchText);
        if (searchText == null || searchText.isEmpty()) {
            TextView tvSearchTextSeparator = findViewById(R.id.tvSearchTextSeparator);
            tvSearchText.setVisibility(View.GONE);
            tvSearchTextSeparator.setVisibility(View.GONE);
        } else {
            tvSearchText.setText(searchText);
        }

        TextView tvCategory = findViewById(R.id.tvCategory);
        if (searchCategories == null || searchCategories.isEmpty()) {
            tvCategory.setText(getString(R.string.search_result_all_categories));
        } else {
            if (searchCategories.size() == 1)
                tvCategory.setText(searchCategories.get(0).toString());
            else {
                tvCategory.setText(String.format(getResources().getConfiguration().locale, "%d %s", searchCategories.size(), getString(R.string.search_result_catogories_count)));
            }

        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());
        searchBookings();
    }

    private void searchBookings() {
        Fragment loadingFragment = new LoadingFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.booking_items_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

        BookingItemQuery query = new BookingItemQuery(searchText, searchCity, null, searchCategories);
        Traveler.searchBookingItems(query, this);
    }

    @Override
    public void onBookingSearchSuccess(BookingItemSearchResult searchResult) {
        llSortAndFilters.setVisibility(View.VISIBLE);

        TextView textView = findViewById(R.id.booking_search_result_total);
        textView.setText(String.format(Locale.getDefault(), "%d items found", searchResult.getTotal()));

        this.currentPriceRangeFilter =
                new PriceRangeFilter(new Range<Double>(searchResult.getFacets().getMinPrice().getValue(TravelerUI.getPreferredCurrency()), searchResult.getFacets().getMaxPrice().getValue(TravelerUI.getPreferredCurrency())), TravelerUI.getPreferredCurrency());


        //only if its first time populate the initial price range
        if (this.initialPriceRangeFilter == null) {
            this.initialPriceRangeFilter = currentPriceRangeFilter;
        }

        Fragment fragment = BookingSearchResultFragment.newInstance(searchResult);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.booking_items_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onBookingSearchError(Error error) {
        RetryFragment errorFragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.booking_items_container, errorFragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onRetry() {
        searchBookings();
    }

    @Override
    public void onSortAndFilterChanged(BookingItemSort bookingItemSort, PriceRangeFilter priceRangeFilter) {
        this.currentPriceRangeFilter = priceRangeFilter;
        this.currentBookingItemSort = bookingItemSort;
        BookingItemQuery query = new BookingItemQuery(searchText, searchCity, null, bookingItemSort, priceRangeFilter, null, null);
        Traveler.searchBookingItems(query, this);
    }
}
