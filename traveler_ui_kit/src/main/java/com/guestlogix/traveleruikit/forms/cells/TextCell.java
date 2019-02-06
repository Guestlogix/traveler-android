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

    // Sets all
    private void init() {
        editText = itemView.findViewById(R.id.input);

        editText.setOnLongClickListener(v -> {
            if (this.mLongClickListener != null) {
                return this.mLongClickListener.onLongClick(this);
            }
            return false;
        });

        editText.setOnClickListener(v -> {
            if (null != this.mClickListener) {
                this.mClickListener.onClick(this);
            }
        });

        FormCell selfRef = this;

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != mTextChangesListener) {
                    mTextChangesListener.onTextChanged(selfRef, s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
