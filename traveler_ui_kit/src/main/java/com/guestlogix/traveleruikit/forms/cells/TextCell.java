package com.guestlogix.traveleruikit.forms.cells;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

/**
 * Form cell which contains an EditText
 * Implements:
 * {@link OnTextChangedListener}
 * {@link OnFocusChangedListener}
 */
public class TextCell extends BaseCell {
    private EditText editText;

    private OnTextChangedListener onTextChangedListener;

    private OnFocusChangedListener onFocusChangedListener;

    public TextCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    @Override
    public void reload() {
        hideError();
    }

    public void setHint(String hint) {
        editText.setHint(hint);
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

    public void setOnFocusChangedListener(OnFocusChangedListener onFocusChangedListener) {
        this.onFocusChangedListener = onFocusChangedListener;
    }

    public void setError(String error) {
        editText.setError(error);
    }

    public void hideError() {
        editText.setError(null);
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

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (null != onFocusChangedListener) {
                onFocusChangedListener.onFocusChanged(hasFocus);
            }
        });
    }

    public interface OnTextChangedListener {
        void onTextChanged(CharSequence value);
    }

    public interface OnFocusChangedListener {
        void onFocusChanged(boolean hasFocus);
    }
}
