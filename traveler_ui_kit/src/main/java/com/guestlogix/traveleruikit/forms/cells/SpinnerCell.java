package com.guestlogix.traveleruikit.forms.cells;

import android.text.InputType;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.SpinnerFormModel;

/**
 * Form cell which contains a spinner.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellValueChangedListener}
 */
public class SpinnerCell extends BaseCell {
    private AutoCompleteTextView autocomplete;
    private TextInputLayout layout;

    public SpinnerCell(@NonNull View itemView) {
        super(itemView);

        this.autocomplete = itemView.findViewById(R.id.autocomplete_form_inputValue);
        this.layout = itemView.findViewById(R.id.textInputLayout_form_textLayout);

        this.autocomplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != SpinnerCell.this.onCellValueChangedListener) {
                    SpinnerCell.this.onCellValueChangedListener.onCellValueChanged(SpinnerCell.this, position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        this.autocomplete.setOnItemClickListener((parent, view, position, id) -> {
            if (null != SpinnerCell.this.onCellValueChangedListener) {
                SpinnerCell.this.onCellValueChangedListener.onCellValueChanged(SpinnerCell.this, position);
            }
        });

        this.autocomplete.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                this.autocomplete.showDropDown();
            }
        });

        this.autocomplete.setOnClickListener(v -> this.autocomplete.showDropDown());
        this.autocomplete.setThreshold(0);
        this.autocomplete.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void reload() {
        autocomplete.setText(null);
        autocomplete.setError(null);
        autocomplete.setHint(null);
        autocomplete.setAdapter(null);
        autocomplete.clearFocus();
    }

    /**
     * Expecting {@link SpinnerFormModel} for binding. Otherwise throws {@link RuntimeException}.
     *
     * @param model description of the cell.
     */
    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (!(model instanceof SpinnerFormModel)) {
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
