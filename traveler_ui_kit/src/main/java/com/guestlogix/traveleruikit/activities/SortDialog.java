package com.guestlogix.traveleruikit.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.models.BookingItemSort;
import com.guestlogix.traveleruikit.R;

public class SortDialog extends Dialog {

    private SortDialogCallback sortDialogCallback;
    private BookingItemSort currentSort;

    public SortDialog(@NonNull Context context, SortDialogCallback sortDialogCallback, @NonNull BookingItemSort currentSort) {
        super(context);
        this.sortDialogCallback = sortDialogCallback;
        this.currentSort = currentSort;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sort);

        RadioGroup rdgSortTypes = findViewById(R.id.rdgSort);

        RadioButton selectedRadioButton = null;
        for (BookingItemSort bookingItemSort : BookingItemSort.getAllPossibleSortTypes()) {
            RadioButton rdb = new RadioButton(getContext());
            rdb.setLayoutParams(new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT));
            rdb.setText(bookingItemSort.toString());
            rdb.setTag(bookingItemSort);

            if (currentSort.equals(bookingItemSort))
                selectedRadioButton = rdb;
            rdgSortTypes.addView(rdb);
        }

        rdgSortTypes.check(selectedRadioButton.getId());

        ((TextView) findViewById(R.id.tvCurrent)).setText(String.format("%s  %s", getContext().getString(R.string.sort_dialog_label), currentSort.toString()));

        findViewById(R.id.btnApply).setOnClickListener(v -> {
            int selectedId = rdgSortTypes.getCheckedRadioButtonId();

            BookingItemSort bookingItemSort = (BookingItemSort) findViewById(selectedId).getTag();
            sortDialogCallback.onSortChanged(bookingItemSort);

            dismiss();
        });
    }

    public interface SortDialogCallback {
        void onSortChanged(BookingItemSort bookingItemSort);
    }
}
