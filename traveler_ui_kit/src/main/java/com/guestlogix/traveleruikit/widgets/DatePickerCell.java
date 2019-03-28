package com.guestlogix.traveleruikit.widgets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveleruikit.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerCell extends FrameLayout {

    // Views
    private TextInputLayout dateLayout;
    private TextInputEditText dateEditText;

    private Calendar calendar;
    // Data
    private Calendar minDate;
    private Calendar maxDate;

    /**
     * Listener to dispatch date change events.
     */
    private OnDateChangedListener onDateChangedListener;

    /**
     * Callback interface for date change events.
     */
    public interface OnDateChangedListener {
        void onDateChanged(Calendar c);
    }


    public DatePickerCell(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public DatePickerCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public DatePickerCell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DatePickerCell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_date_picker_cell, this, true);
            dateLayout = view.findViewById(R.id.dateLayout);
            dateEditText = view.findViewById(R.id.dateEditText);

            dateEditText.setOnClickListener(this::onClickEventHandler);
        }
    }

    public void setDate(Calendar calendar) {
        if (calendar != null) {
            this.calendar = calendar;
            updateDateLabel();
        }
    }

    public void setError(@Nullable String error) {
        dateEditText.setError(error);
    }

    public void setHint(@Nullable String hint) {
        this.dateEditText.setHint(hint);
    }

    /**
     * Registers a callback for date change events.
     *
     * @param l Callback interface for date change events.
     */
    public void setOnDateChangedListener(OnDateChangedListener l) {
        this.onDateChangedListener = l;
    }

    public void setMinDate(Calendar minDate) {
        this.minDate = minDate;
    }

    public void setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
    }

    private void onClickEventHandler(View v) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
            setError(null);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            final DatePickerDialog d = new DatePickerDialog(getContext(), this::onDateSetEventHandler, year, month, day);

            if (minDate != null) {
                d.getDatePicker().setMinDate(minDate.getTimeInMillis());
            }

            if (maxDate != null) {
                d.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            }

            d.show();
    }

    private void onDateSetEventHandler(DatePicker d, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        updateDateLabel();

        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(calendar);
        }
    }

    private void updateDateLabel() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
        String label = df.format(calendar.getTime());
        dateEditText.setText(label);
    }

    private Locale getLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            return getResources().getConfiguration().locale;
        }
    }
}
