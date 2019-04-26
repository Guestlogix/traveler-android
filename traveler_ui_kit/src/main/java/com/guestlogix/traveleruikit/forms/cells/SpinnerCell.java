package com.guestlogix.traveleruikit.forms.cells;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.FormFieldType;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.SpinnerFormModel;

/**
 * Form cell which contains a spinner.
 */
public class SpinnerCell extends BaseCell {
    private AutoCompleteTextView autocomplete;
    private TextInputLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    public SpinnerCell(@NonNull View itemView) {
        super(itemView);

        autocomplete = itemView.findViewById(R.id.autocomplete_form_inputValue);
        layout = itemView.findViewById(R.id.textInputLayout_form_textLayout);

        this.autocomplete.setOnItemClickListener((parent, view, position, id) -> {
            if (null != cellEventsListener) {
                cellEventsListener.onCellValueChanged(SpinnerCell.this, position);
            }
        });

        this.autocomplete.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_UP) {
                Context c = contextRequestListener.onCellContextRequest();

                InputMethodManager imm = (InputMethodManager) c.getSystemService(Activity.INPUT_METHOD_SERVICE);

                if (imm != null) {
                    imm.hideSoftInputFromWindow(itemView.getWindowToken(), 0);
                }

                this.autocomplete.showDropDown();
            }

            return false;
        });

        this.autocomplete.setInputType(InputType.TYPE_NULL);
    }

    /**
     * Expecting {@link SpinnerFormModel} for binding. Otherwise throws {@link RuntimeException}.
     *
     * @param model description of the cell.
     */
    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (model.getType() != FormFieldType.SPINNER) {
            throw new RuntimeException("Expecting SpinnerFormModel, but got " + model.getClass().getName());
        }

        SpinnerFormModel s = (SpinnerFormModel) model;

        layout.setHint(s.getHint());

        if (s.getOptions() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(contextRequestListener.onCellContextRequest(),
                    android.R.layout.simple_list_item_1, s.getOptions());

            autocomplete.setAdapter(adapter);
        }

        Integer pos = (Integer) cellValueAdapter.getCellValue(this);

        if (pos != null) {
            autocomplete.showDropDown();
            autocomplete.onCommitCompletion(new CompletionInfo(0, pos, null));
        } else {
            autocomplete.setText(null);
        }
    }

    @Override
    public void setMessage(@Nullable FormMessage message) {
        if (message == null) {
            layout.setError(null);
            layout.setHelperText(null);
        } else {
            switch (message.getType()) {
                case INFO:
                    layout.setError(null);
                    layout.setHelperText(message.getMessage());
                    break;
                case ALERT:
                    layout.setHelperText(null);
                    layout.setError(message.getMessage());
            }
        }
    }

}
