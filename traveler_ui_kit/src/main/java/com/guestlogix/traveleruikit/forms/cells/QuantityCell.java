package com.guestlogix.traveleruikit.forms.cells;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.QuantityFormModel;

/**
 * Cell which contains a quantityIndicator picker.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellValueChangedListener}
 */
public class QuantityCell extends BaseCell {
    private TextView quantityIndicator;
    private TextView title;
    private TextView subTitle;

    public QuantityCell(@NonNull View itemView) {
        super(itemView);

        quantityIndicator = itemView.findViewById(R.id.quantity);
        title = itemView.findViewById(R.id.title);
        subTitle = itemView.findViewById(R.id.subTitle);
    }

    /**
     * Expects a {@link QuantityFormModel} for binding. Otherwise throws {@link RuntimeException}.
     * Might invoke {@link CellValueAdapter} multiple times.
     *
     * @param model description of the cell.
     */
    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (!(model instanceof QuantityFormModel)) {
            throw new RuntimeException("Expecting QuantityFormModel, but got " + model.getClass().getName());
        }

        QuantityFormModel q = (QuantityFormModel) model;

        title.setText(q.getTitle());
        subTitle.setText(q.getSubtitle());

        Integer val = (Integer) cellValueAdapter.getCellValue(this);
        quantityIndicator.setText(val != null ? val.toString() : "0");

        quantityIndicator.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(contextRequestListener.onCellContextRequest());
            dialog.setContentView(R.layout.number_picker_dialog);

            Button acceptBtn = dialog.findViewById(R.id.okButton);
            Button cancelBtn = dialog.findViewById(R.id.cancelButton);
            TextView title = dialog.findViewById(R.id.numberPickerTitle);
            NumberPicker picker = dialog.findViewById(R.id.numberPicker);

            // Title
            title.setText(q.getTitle());

            // Number picker
            picker.setMaxValue(q.getMaxValue() != null ? q.getMaxValue() : Integer.MAX_VALUE);
            picker.setMinValue(q.getMinValue() != null ? q.getMinValue() : Integer.MIN_VALUE);
            Integer i = (Integer) cellValueAdapter.getCellValue(this);
            picker.setValue(i != null ? i : q.getMinValue());

            // Buttons
            cancelBtn.setOnClickListener(v1 -> dialog.dismiss());
            acceptBtn.setOnClickListener(v1 -> {
                Integer value = picker.getValue();
                quantityIndicator.setText(String.valueOf(value));

                if (null != onCellValueChangedListener) {
                    onCellValueChangedListener.onCellValueChanged(this, value);
                }

                dialog.dismiss();
            });

            dialog.show();
        });
    }

    @Override
    public void reload() {
        quantityIndicator.setText("0");
    }
}
