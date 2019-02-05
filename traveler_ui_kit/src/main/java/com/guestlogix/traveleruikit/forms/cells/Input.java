package com.guestlogix.traveleruikit.forms.cells;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

import static android.view.View.VISIBLE;


public class Input extends FormCell {

    public Input(@NonNull View itemView) {
        super(itemView);
        init();
    }

    public void reload() {
        setText("");
        hideError();
        hideInfo();

    }

    public void showInfo(String info) {
        TextView tv = itemView.findViewById(R.id.info);
        tv.setText(info);
        tv.setVisibility(VISIBLE);
    }

    public void showError(String error) {
        TextView tv = itemView.findViewById(R.id.error);
        tv.setText(error);
        tv.setVisibility(VISIBLE);
    }

    public void setText(String text) {
        EditText editText = itemView.findViewById(R.id.input);
        editText.setText(text);
    }

    public void setLabel(String text) {
        TextView tv = itemView.findViewById(R.id.label);
        tv.setText(text);
    }

    public void hideInfo() {
        TextView tv = itemView.findViewById(R.id.info);
        tv.setVisibility(itemView.GONE);
    }

    public void hideError() {
        TextView tv = itemView.findViewById(R.id.error);
        tv.setVisibility(itemView.GONE);
    }

    private void init() {
        EditText text = itemView.findViewById(R.id.input);

        text.setOnLongClickListener(v -> {
            if (this.mLongClickListener != null) {
                return this.mLongClickListener.onLongClick(this);
            }
            return false;
        });

        text.setOnClickListener(v -> {
            if (null != this.mClickListener) {
                this.mClickListener.onClick(this);
            }
        });

        FormCell selfRef = this;

        text.addTextChangedListener(new TextWatcher() {
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

    public static int getResourceId() {
        return R.layout.view_input;
    }
}
