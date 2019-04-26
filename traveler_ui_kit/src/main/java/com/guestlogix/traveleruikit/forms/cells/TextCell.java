package com.guestlogix.traveleruikit.forms.cells;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.FormFieldType;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.TextFormModel;

/**
 * Form cell which contains an EditText
 */
public class TextCell extends BaseCell {
    private TextInputEditText input;
    private TextInputLayout layout;

    public TextCell(@NonNull View itemView) {
        super(itemView);
        input = itemView.findViewById(R.id.editText_form_textInput);
        layout = itemView.findViewById(R.id.textInputLayout_form_textLayout);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != cellEventsListener) {
                    cellEventsListener.onCellValueChanged(TextCell.this, s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing.
            }
        });

        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (null != cellEventsListener) {
                cellEventsListener.onCellFocusChange(this, hasFocus);
            }
        });
    }

    /**
     * Expecting {@link TextFormModel} for binding. Otherwise throws {@link RuntimeException}
     *
     * @param model description of the cell.
     */
    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (model.getType() != FormFieldType.TEXT) {
            throw new RuntimeException("Expecting TextFormModel, but got " + model.getClass().getName());
        }

        TextFormModel t = (TextFormModel) model;

        layout.setHint(t.getHint());
        Object value = cellValueAdapter.getCellValue(this);
        input.setText(value != null ? value.toString() : null);
    }

    @Override
    public void setMessage(@Nullable FormMessage message) {
        if (message == null) {
            layout.setError(null);
            input.setError(null);
            layout.setHelperText(null);
        } else {
            switch (message.getType()) {
                case INFO:
                    layout.setError(null);
                    input.setError(null);
                    layout.setHelperText(message.getMessage());
                    break;
                case ALERT:
                    layout.setHelperText(null);
                    input.setError(message.getMessage());
                    layout.setError(message.getMessage());
            }
        }
    }
}
