package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

/**
 * A Button input cell type.
 * Expects a layout containing exactly 4 different fields.
 * <p>
 * 1. A Label TextView with the android:id 'label'
 * 2. A Button with the android:id 'button'
 * 3. An Info TextView with the android:id 'info'
 * 4. An Error TextView with the android:id 'error'
 */
public class ButtonCell extends FormCell {

    public ButtonCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    @Override
    public void reload() {

    }

    /**
     * Sets the specific listeners for this cell.
     */
    private void init() {
        Button b = itemView.findViewById(R.id.button);

        b.setOnClickListener(v -> {
            if (null != mClickListener) {
                mClickListener.onClick(this);
            }
        });

        b.setOnLongClickListener(v -> mLongClickListener != null && mLongClickListener.onLongClick(this));
    }

    public void showInfo(String info) {
        TextView t = itemView.findViewById(R.id.info);

        if (null != t) {
            t.setText(info);
            t.setVisibility(View.VISIBLE);
        }
    }

    public void hideInfo() {
        TextView t = itemView.findViewById(R.id.info);

        if (null != t) {
            t.setText("");
            t.setVisibility(View.GONE);
        }
    }

    public void showError(String error) {
        TextView t = itemView.findViewById(R.id.error);

        if (null != t) {
            t.setText(error);
            t.setVisibility(View.VISIBLE);
        }
    }

    public void hideError() {
        TextView t = itemView.findViewById(R.id.error);

        if (null != t) {
            t.setText("");
            t.setVisibility(View.GONE);
        }
    }

    public void setText(String text) {
        Button b = itemView.findViewById(R.id.button);

        if (null != b) {
            b.setText(text);
        }
    }
}
