package com.guestlogix.traveleruikit.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveleruikit.R;

import java.util.List;

public class ListPickerCell extends FrameLayout {

    // Views
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autocomplete;

    /**
     * Listener to dispatch item selection events.
     */
    private OnItemSelectedListener onItemSelectedListener;

    public void setValueList(List<String> cellLabels) {
        autocomplete.clearListSelection();
        autocomplete.setText(null);

        if (cellLabels != null && !cellLabels.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    R.layout.option_dropdown_item, R.id.textView_optionDropdown_label, cellLabels);

            autocomplete.setAdapter(adapter);

        }
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

    public void setError(@Nullable String error) {
        textInputLayout.setError(error);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context c, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(c).inflate(R.layout.view_list_picker_cell, this, true);
            textInputLayout = view.findViewById(R.id.textLayout);
            autocomplete = view.findViewById(R.id.autocomplete);
            autocomplete.setOnItemClickListener(this::onItemSelected);

            autocomplete.setOnTouchListener((v, e) -> {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    autocomplete.showDropDown();
                }

                return false;
            });
        }
    }

    public void setHint(@Nullable String hint) {
        textInputLayout.setHint(hint);
    }

    /**
     * Registers callback for item selected events.
     *
     * @param l Callback interface for item selection events.
     */
    public void setOnItemSelectedListener(@Nullable OnItemSelectedListener l) {
        this.onItemSelectedListener = l;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(position);
        }
    }

    /**
     * Callback interface for item selection events.
     */
    public interface OnItemSelectedListener {
        /**
         * Called whenever an item is selected.
         *
         * @param position position of the item in the list
         */
        void onItemSelected(int position);
    }
}
