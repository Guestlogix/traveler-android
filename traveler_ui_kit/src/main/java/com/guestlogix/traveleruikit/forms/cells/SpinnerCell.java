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

import java.util.List;

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
     * Populates the view with a list of options and sets the current option to the given index. Does not check whether
     * the option is currently selected.
     *
     * @param options  Items to display in the dropdown list.
     * @param position Optional element to select in the list.
     */
    public void setOptions(List<String> options, @Nullable Integer position) {
        if (null != contextRequestListener && null != autocomplete) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(contextRequestListener.onCellContextRequest(),
                    android.R.layout.simple_list_item_1, options);

            autocomplete.setAdapter(adapter);
        }

        if (null != autocomplete && position != null) {
            autocomplete.showDropDown();
            autocomplete.onCommitCompletion(new CompletionInfo(0, position, null));
        }
    }

    public void setHint(String hint) {
        this.layout.setHint(hint);
    }
}
