package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

import java.util.List;

/**
 * Form cell which contains a spinner.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellValueChangedListener}
 */
public class SpinnerCell extends BaseCell {
    private TextView title;
    private TextView subtitle;
    private Spinner spinner;

    public SpinnerCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    @Override
    public void reload() {

    }

    /**
     * Populates the view with the list of options.
     *
     * @param options Items to display in the dropdown list.
     */
    public void setOptions(List<String> options) {
        if (null != contextRequestListener && null != spinner) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(contextRequestListener.onCellContextRequest(),
                    android.R.layout.simple_spinner_item, options);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

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

        if (null != spinner) {
            spinner.setSelection(position);
        }
    }

    /**
     * Sets the title of the view with the given String.
     *
     * @param title Title to display on the view.
     */
    public void setTitle(String title) {
        if (null != this.title) {
            this.title.setText(title);
        }
    }

    /**
     * Sets the subtitle of the view with the given String.
     *
     * @param subtitle Sub Title to display on the view.
     */
    public void setSubtitle(String subtitle) {
        if (null != this.subtitle) {
            this.subtitle.setText(subtitle);
        }
    }

    private void init() {
        this.title = itemView.findViewById(R.id.title);
        this.subtitle = itemView.findViewById(R.id.subTitle);
        this.spinner = itemView.findViewById(R.id.spinner);

        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != SpinnerCell.this.onCellValueChangedListener) {
                    SpinnerCell.this.onCellValueChangedListener.onCellValueChanged(SpinnerCell.this, Integer.valueOf(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
}
