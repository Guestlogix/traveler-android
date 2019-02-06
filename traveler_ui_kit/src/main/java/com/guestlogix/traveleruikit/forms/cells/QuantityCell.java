package com.guestlogix.traveleruikit.forms.cells;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

public class QuantityCell extends FormCell {
    private EditText quantity;
    private QuantityCellAdapter adapter;

    public QuantityCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    @Override
    public void reload() {

    }

    private void init () {
        quantity = itemView.findViewById(R.id.quantity);

        // TODO: make this cleaner.
        quantity.setOnClickListener(v -> {
            final Dialog d = new Dialog(itemView.getContext());
            d.setTitle(adapter.getTitle());
            d.setContentView(R.layout.number_picker_dialog);

            Button accept = d.findViewById(R.id.button1);
            Button cancel = d.findViewById(R.id.button2);
            NumberPicker np = d.findViewById(R.id.numberPicker);

            if (adapter.isMaxQuantityRequired()) {
                np.setMaxValue(adapter.getMaxQuantity());
            }

            np.setMinValue(0);

            accept.setOnClickListener(v2 -> {
                quantity.setText(String.valueOf(np.getValue()));
                d.dismiss();
            });

            cancel.setOnClickListener( v2 -> d.dismiss());

            d.show();
        });
    }

    public void setAdapter (QuantityCellAdapter adapter) {
        this.adapter = adapter;
    }

    public interface QuantityCellAdapter {
        String getTitle();
        boolean isMaxQuantityRequired();
        int getMaxQuantity();
        int getMinQuantity();
    }
}
