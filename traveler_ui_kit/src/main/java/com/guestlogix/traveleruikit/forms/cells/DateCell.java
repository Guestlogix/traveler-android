package com.guestlogix.traveleruikit.forms.cells;

import android.app.DatePickerDialog;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.models.DateFormModel;
import com.guestlogix.traveleruikit.forms.models.FormModel;

import java.text.DateFormat;
import java.util.Calendar;
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

    public DateCell(@NonNull View itemView) {
        super(itemView);
        dateInputLayout = itemView.findViewById(R.id.dateLayout);
        input = itemView.findViewById(R.id.input);

        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (null != this.onCellFocusChangeListener) {
                this.onCellFocusChangeListener.onCellFocusChange(this, hasFocus);
            }
        });
    }

    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (!(model instanceof DateFormModel)) {
            throw new RuntimeException("Expecting DateFormModel, but got " + model.getClass().getName());
        }

        DateFormModel d = (DateFormModel) model;

        dateInputLayout.setHint(d.getHint());

        Calendar val = (Calendar) cellValueAdapter.getCellValue(this);

        if (val == null) {
            val = Calendar.getInstance();
            input.setText(null);
        } else {
            updateDateLabel(val);
        }

        // Copy for final ref.
        Calendar calendar = val;
        input.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            final DatePickerDialog dialog = new DatePickerDialog(contextRequestListener.onCellContextRequest(), this::onDateSetEventHandler, year, month, day);

            if (d.getMaxDate() != null) {
                dialog.getDatePicker().setMaxDate(d.getMaxDate().getTime());
            }

            if (d.getMinDate() != null) {
                dialog.getDatePicker().setMinDate(d.getMinDate().getTime());
            }

            dialog.show();
        });
    }

    private void updateDateLabel(Calendar calendar) {
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

    private void onDateSetEventHandler(DatePicker d, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        onCellValueChangedListener.onCellValueChanged(DateCell.this, calendar);

        updateDateLabel(calendar);
    }

    @Override
    public void reload() {
        input.setText(null);
        dateInputLayout.setHint(null);
        input.clearFocus();
    }
}
