package com.guestlogix.traveleruikit.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.models.PriceRangeFilter;
import com.guestlogix.travelercorekit.models.Range;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;

public class PriceRangeDialog extends Dialog {

    private PriceRangeDialogCallback priceRangeDialogCallback;
    private PriceRangeFilter currentPriceRange, initialPriceRange;

    public PriceRangeDialog(@NonNull Context context, PriceRangeDialogCallback priceRangeDialogCallback, PriceRangeFilter currentRangeFilter, PriceRangeFilter initialPriceRange) {
        super(context);
        this.priceRangeDialogCallback = priceRangeDialogCallback;
        this.currentPriceRange = currentRangeFilter;
        this.initialPriceRange = initialPriceRange;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_price_range);
        TextView etMax = findViewById(R.id.etMax);
        TextView etMin = findViewById(R.id.etMin);

        etMin.setText(String.valueOf(currentPriceRange.getRange().getLower()));
        etMax.setText(String.valueOf(currentPriceRange.getRange().getUpper()));

        ((TextView) findViewById(R.id.tvAvailableRange)).setText(String.format("%s %s", getContext().getString(R.string.price_range_dialog_title), initialPriceRange));

        findViewById(R.id.btnApply).setOnClickListener(v -> {

            double minPrice = Double.parseDouble(etMin.getText().toString());
            double maxPrice = Double.parseDouble(etMax.getText().toString());
            if (minPrice > maxPrice) {
                Toast.makeText(getContext(), "Min Value Can Not Be Less Than Max Value", Toast.LENGTH_LONG).show();
                return;
            }
            priceRangeDialogCallback.onPriceRangeChanged(new PriceRangeFilter(new Range<>(minPrice, maxPrice), TravelerUI.getPreferredCurrency()));
            dismiss();
        });
    }

    public interface PriceRangeDialogCallback {
        void onPriceRangeChanged(PriceRangeFilter priceRangeFilter);
    }
}
