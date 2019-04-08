package com.guestlogix.traveleruikit.forms.cells;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.widgets.DatePickerCell;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Form cell which contains an EditText
 * Implements:
 * {@link OnCellValueChangedListener} with value of type {@link CharSequence}
 * {@link OnCellFocusChangeListener}
 */
public class DateCell extends BaseCell {
    private TextInputLayout dateInputLayout;
    private TextInputEditText input;

    private Calendar calendar;

    public DateCell(@NonNull View itemView) {
        super(itemView);
        dateInputLayout = itemView.findViewById(R.id.dateLayout);
        input = itemView.findViewById(R.id.input);

        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (null != this.onCellFocusChangeListener) {
                this.onCellFocusChangeListener.onCellFocusChange(this, hasFocus);
            }
        });

        input.setOnClickListener(this::onClickEventHandler);
    }

    @Override
    public void setModel(@NonNull FormModel model) {

    }

    public void setDate(Date date) {
        if (calendar != null) {
            this.calendar = Calendar.getInstance();
            this.calendar.setTime(date);
            updateDateLabel();
        }
    }

    private void updateDateLabel() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
        String label = df.format(calendar.getTime());
        input.setText(label);
    }

    private Locale getLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return contextRequestListener.onCellContextRequest().getResources().getConfiguration().getLocales().get(0);
        } else {
            return contextRequestListener.onCellContextRequest().getResources().getConfiguration().locale;
        }
    }

    private void onClickEventHandler(View v) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        //setError(null);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog d = new DatePickerDialog(contextRequestListener.onCellContextRequest(), this::onDateSetEventHandler, year, month, day);

        d.show();

    }

    private void onDateSetEventHandler(DatePicker d, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        DateCell.this.onCellValueChangedListener.onCellValueChanged(DateCell.this, calendar);
        updateDateLabel();
    }

    @Override
    public void reload() {
        input.setText(null);
        dateInputLayout.setHint(null);
        input.clearFocus();
    }

    public void setHint(String hint) {
        dateInputLayout.setHint(hint);
    }

    public void setValue(CharSequence value) {
        if (null != value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                input.setText(Html.fromHtml(value.toString(), Html.FROM_HTML_MODE_COMPACT).toString());
            } else {
                input.setText(Html.fromHtml(value.toString()).toString());
            }
        }
    }
}
