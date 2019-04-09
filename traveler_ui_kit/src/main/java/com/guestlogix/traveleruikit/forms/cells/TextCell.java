package com.guestlogix.traveleruikit.forms.cells;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.TextFormModel;

/**
 * Form cell which contains an EditText
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellValueChangedListener} with value of type {@link CharSequence}
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellFocusChangeListener}
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
                if (null != TextCell.this.onCellValueChangedListener) {
                    TextCell.this.onCellValueChangedListener.onCellValueChanged(TextCell.this, s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing.
            }
        });

        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (null != this.onCellFocusChangeListener) {
                this.onCellFocusChangeListener.onCellFocusChange(this, hasFocus);
            }
        });
    }

    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (!(model instanceof TextFormModel)) {
            throw new RuntimeException("Expecting TextFormModel, but got " + model.getClass().getName());
        }

        TextFormModel t = (TextFormModel) model;

        layout.setHint(t.getHint());
        Object value = cellValueAdapter.getCellValue(this);
        input.setText(value != null ? value.toString() : null);
    }

    @Override
    public void reload() {
        input.setText(null);
        layout.setHint(null);
        input.clearFocus();
    }
}
