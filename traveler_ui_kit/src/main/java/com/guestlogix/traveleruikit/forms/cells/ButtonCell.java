package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.models.ButtonFormModel;
import com.guestlogix.traveleruikit.forms.models.FormModel;

/**
 * A Button input cell type.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellClickListener}
 */
public class ButtonCell extends BaseCell {

    private Button button;

    public ButtonCell(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.button);

        button.setOnClickListener(v -> {
            if (null != onCellClickListener) {
                onCellClickListener.onCellClick(this);
            }
        });
    }

    @Override
    public void reload() {
        // Do nothing.
    }

    @Override
    public void setModel(@NonNull FormModel model) {
        if (!(model instanceof ButtonFormModel)) {
            throw new RuntimeException("Expecting ButtonFormModel, but got " + model.getClass().getName());
        }

        ButtonFormModel b = (ButtonFormModel) model;

        button.setText(b.getText());
    }
}
