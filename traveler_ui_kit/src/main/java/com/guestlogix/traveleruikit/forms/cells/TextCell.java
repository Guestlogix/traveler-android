package com.guestlogix.traveleruikit.forms.cells;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

/**
 * View holder which can hide
 */
public class TextCell extends FormCell {
    private EditText editText;

    private OnTextChangedListener onTextChangedListener;

    public TextCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    @Override
    public void reload() {

    }

    public void setHint(String hint) {
        editText.setHint(hint);
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

    private void init() {
        editText = itemView.findViewById(R.id.input);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != onTextChangedListener) {
                    onTextChangedListener.onTextChanged(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public interface OnTextChangedListener {
        void onTextChanged(CharSequence value);
    }
}
