package com.guestlogix.traveleruikit.forms.cells;

import android.app.DatePickerDialog;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.FormFieldType;
import com.guestlogix.traveleruikit.forms.models.DateFormModel;
import com.guestlogix.traveleruikit.forms.models.FormModel;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Form cell which contains an EditText
 */
public class DateCell extends BaseCell {
    private TextInputLayout dateInputLayout;
    private TextInputEditText input;

    public DateCell(@NonNull View itemView) {
        super(itemView);
        dateInputLayout = itemView.findViewById(R.id.dateLayout);
        input = itemView.findViewById(R.id.input);

        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (null != cellEventsListener) {
                cellEventsListener.onCellFocusChange(this, hasFocus);
            }
        });
    }

    /**
     * Expects a {@link DateFormModel} for correct binding.
     * Might invoke {@link CellValueAdapter} multiple times.
     *
     * @param model description of the cell.
     */
    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (model.getType() != FormFieldType.DATE) {
            TravelerLog.e("Expecting DateFormModel, but got " + model.getClass().getName());
            return;
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

    @Override
    public void setMessage(@Nullable FormMessage message) {
        if (message == null) {
            dateInputLayout.setError(null);
            dateInputLayout.setHelperText(null);
        } else {
            switch (message.getType()) {
                case INFO:
                    dateInputLayout.setError(null);
                    dateInputLayout.setHelperText(message.getMessage());
                    break;
                case ALERT:
                    dateInputLayout.setHelperText(null);
                    dateInputLayout.setError(message.getMessage());
            }
        }
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
        cellEventsListener.onCellValueChanged(DateCell.this, calendar);

        updateDateLabel(calendar);
    }
}
