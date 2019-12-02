package com.guestlogix.traveleruikit.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.models.BookingItemSort;
import com.guestlogix.travelercorekit.models.PriceRangeFilter;
import com.guestlogix.traveleruikit.R;

public class SortAndFilterDialog extends Dialog implements PriceRangeDialog.PriceRangeDialogCallback, SortDialog.SortDialogCallback {

    private TextView tvSort;
    private TextView tvPriceRange;
    private PriceRangeFilter currentPriceRangeFilter, initialPriceRangeFilter;
    private BookingItemSort currentBookingItemSort, initialBookingItemSort;
    private SortAndFilterDialogCallback sortAndFilterDialogCallback;

    private PriceRangeFilter newPriceRangeFilter;
    private BookingItemSort newBookingItemSort;

    public SortAndFilterDialog(@NonNull Context context,
                               PriceRangeFilter currentPriceRangeFilter,
                               PriceRangeFilter initialPriceRangeFilter,
                               BookingItemSort currentBookingItemSort,
                               BookingItemSort initialBookingItemSort,
                               SortAndFilterDialogCallback sortAndFilterDialogCallback) {
        super(context);
        this.currentPriceRangeFilter = currentPriceRangeFilter;
        this.initialPriceRangeFilter = initialPriceRangeFilter;
        this.currentBookingItemSort = currentBookingItemSort;
        this.initialBookingItemSort = initialBookingItemSort;
        this.sortAndFilterDialogCallback = sortAndFilterDialogCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_booking_search_filters);
        tvSort = findViewById(R.id.tvSort);
        tvPriceRange = findViewById(R.id.tvPriceRange);

        updatePriceRangeOnUi(currentPriceRangeFilter);
        updateSortTypeOnUi(currentBookingItemSort);

        findViewById(R.id.tvPriceRange).setOnClickListener(v -> new PriceRangeDialog(getContext(), SortAndFilterDialog.this, newPriceRangeFilter, initialPriceRangeFilter).show());

        findViewById(R.id.tvSort).setOnClickListener(v -> new SortDialog(getContext(), SortAndFilterDialog.this, newBookingItemSort).show());

        findViewById(R.id.btnApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newPriceRangeFilter.equals(currentPriceRangeFilter) || !newBookingItemSort.equals(currentBookingItemSort)) {
                    sortAndFilterDialogCallback.onSortAndFilterChanged(newBookingItemSort, newPriceRangeFilter);
                }
                dismiss();
            }
        });

        findViewById(R.id.btnReset).setOnClickListener(v -> {
            updatePriceRangeOnUi(initialPriceRangeFilter);
            updateSortTypeOnUi(initialBookingItemSort);
        });
    }

    @Override
    public void onPriceRangeChanged(PriceRangeFilter priceRangeFilter) {
        updatePriceRangeOnUi(priceRangeFilter);
    }

    @Override
    public void onSortChanged(BookingItemSort bookingItemSort) {
        updateSortTypeOnUi(bookingItemSort);
    }

    private void updatePriceRangeOnUi(PriceRangeFilter priceRangeFilter) {
        newPriceRangeFilter = priceRangeFilter;
        tvPriceRange.setText(priceRangeFilter.toString());
    }

    private void updateSortTypeOnUi(BookingItemSort bookingItemSort) {
        newBookingItemSort = bookingItemSort;
        tvSort.setText(bookingItemSort.toString());

    }

    interface SortAndFilterDialogCallback {
        void onSortAndFilterChanged(BookingItemSort bookingItemSort, PriceRangeFilter priceRangeFilter);
    }
}
