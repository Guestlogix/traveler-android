package com.guestlogix.traveleruikit.forms.cells;

import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

/**
 * Form cell which contains an EditText
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellValueChangedListener} with value of type {@link CharSequence}
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellFocusChangeListener}
 */
public class TextCell extends BaseCell {
    private EditText input;

    public TextCell(@NonNull View itemView) {
        super(itemView);
        input = itemView.findViewById(R.id.input);

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
            }
        });

        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (null != this.onCellFocusChangeListener) {
                this.onCellFocusChangeListener.onCellFocusChange(this, hasFocus);
            }
        });
    }

    @Override
    public void reload() {
        input.setText(null);
        input.setHint(null);
    }

    public void setHint(String hint) {
        input.setHint(hint);
    }

    public void setValue(CharSequence value) {
        if (null != value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                input.setText(Html.fromHtml(value.toString(), Html.FROM_HTML_MODE_COMPACT).toString());
            } else {
                input.setText(Html.fromHtml(value.toString()).toString());
            }
        }
    }
}
