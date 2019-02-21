package com.guestlogix.traveleruikit.forms.cells;

import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

import java.util.List;

/**
 * Form cell which contains a spinner.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellValueChangedListener}
 */
public class SpinnerCell extends BaseCell {
    //    private Spinner spinner;
    private AutoCompleteTextView autocomplete;

    public SpinnerCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    @Override
    public void reload() {
        autocomplete.setText(null);
        autocomplete.setError(null);
        autocomplete.setHint(null);
        autocomplete.setAdapter(null);
    }

    /**
     * Populates the view with the list of options.
     *
     * @param options Items to display in the dropdown list.
     */
    public void setOptions(List<String> options) {
        if (null != contextRequestListener && null != autocomplete) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(contextRequestListener.onCellContextRequest(),
                    android.R.layout.simple_list_item_1, options);

//            adapter.setDropDownViewResource(R.layout.form_spinner_dropdown_item);
            autocomplete.setAdapter(adapter);
        }
    }

    /**
     * Populates the view with a list of options and sets the current option to the given index. Does not check whether
     * the option is currently selected.
     *
     * @param options  Items to display in the dropdown list.
     * @param position Element to select after populating the list.
     */
    public void setOptions(List<String> options, int position) {
        setOptions(options);

        if (null != autocomplete) {
//            autocomplete.setSelection(position);
            autocomplete.setText((String) autocomplete.getAdapter().getItem(position));
        }
    }

    /**
     * Displays an error with the autocomplete field.
     *
     * @param error String to display as error.
     */
    public void setError(String error) {
        this.autocomplete.setError(error);
    }

    public void setTitle(String title) {
        this.autocomplete.setHint(title);
    }

    private void init() {
        this.autocomplete = itemView.findViewById(R.id.autocomplete);

        this.autocomplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != SpinnerCell.this.onCellValueChangedListener) {
                    SpinnerCell.this.onCellValueChangedListener.onCellValueChanged(SpinnerCell.this, Integer.valueOf(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        this.autocomplete.setInputType(InputType.TYPE_NULL);
    }
}
