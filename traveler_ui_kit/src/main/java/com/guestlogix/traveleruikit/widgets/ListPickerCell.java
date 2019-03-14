package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.guestlogix.traveleruikit.R;

import java.util.List;

public class ListPickerCell extends FrameLayout {

    // Views
    private EditText valueEditText;
    private Spinner slotSpinner;

    /**
     * Listener to dispatch item selection events.
     */
    private OnItemSelectedListener onItemSelectedListener;

    /**
     * Callback interface for item selection events.
     */
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    public ListPickerCell(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ListPickerCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ListPickerCell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ListPickerCell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setValueList(List<String> cellLabels) {
        if (cellLabels != null && !cellLabels.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.option_dropdown_item, R.id.textView_optionDropdown_label, cellLabels);

            slotSpinner.setAdapter(adapter);
            slotSpinner.setOnItemSelectedListener(onSpinnerItemSelectedListener);
        }
    }

    public void setError(@Nullable String error) {
        valueEditText.setError(error);
    }

    public void setHint(@Nullable String hint) {
        valueEditText.setHint(hint);
    }

    /**
     * Registers callback for item selected events.
     *
     * @param l Callback interface for item selection events.
     */
    public void setOnItemSelectedListener(@Nullable OnItemSelectedListener l) {
        this.onItemSelectedListener = l;
    }

    private void init(Context c, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(c).inflate(R.layout.view_list_picker_cell, this, true);
            valueEditText = view.findViewById(R.id.value);
            slotSpinner = view.findViewById(R.id.slotsSpinner);

            valueEditText.setOnClickListener(v -> slotSpinner.performClick());
        }
    }

    AdapterView.OnItemSelectedListener onSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String value = (String) parent.getItemAtPosition(position);
            valueEditText.setText(value);

            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelected(position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    };
}
