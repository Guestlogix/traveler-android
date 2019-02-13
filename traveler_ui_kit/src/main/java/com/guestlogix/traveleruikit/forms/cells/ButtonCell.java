package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

/**
 * A Button input cell type.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellClickListener}
 */
public class ButtonCell extends BaseCell {

    private Button button;
    private TextView title;

    public ButtonCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    @Override
    public void reload() {

    }

    public void setText(String text) {
        if (null != button) {
            button.setText(text);
        }
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    /**
     * Sets the specific listeners for this cell.
     */
    private void init() {
        button = itemView.findViewById(R.id.button);
        title = itemView.findViewById(R.id.title);

        button.setOnClickListener(v -> {
            if (null != onCellClickListener) {
                onCellClickListener.onCellClick(this);
            }
        });
    }
}
