package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.models.ButtonFormModel;
import com.guestlogix.traveleruikit.forms.models.FormModel;

/**
 * A Button input cell type.
 */
public class ButtonCell extends BaseCell {

    private Button button;

    public ButtonCell(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.button);

        button.setOnClickListener(v -> {
            if (null != cellEventsListener) {
                cellEventsListener.onCellClick(this);
            }
        });
    }

    /**
     * Expects a {@link ButtonFormModel} for correct binding. Otherwise a {@link RuntimeException} will be thrown.
     *
     * @param model description of the cell.
     */
    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (!(model instanceof ButtonFormModel)) {
            throw new RuntimeException("Expecting ButtonFormModel, but got " + model.getClass().getName());
        }

        ButtonFormModel b = (ButtonFormModel) model;

        button.setText(b.getText());
    }

    @Override
    public void setMessage(@Nullable FormMessage message) {
        // Do nothing.
    }
}
