package com.guestlogix.traveleruikit.forms.cells;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

public class QuantityCell extends BaseCell {
    private TextView quantity;
    private TextView title;
    private TextView subTitle;
    private String sTitle;

    private QuantityCellAdapter adapter;

    private OnQuantityChangedListener onQuantityChanged;

    public QuantityCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    @Override
    public void reload() {
        quantity.setText("");
    }

    public void setAdapter (QuantityCellAdapter adapter) {
        this.adapter = adapter;
    }

    public void setOnQuantityChangedListener(OnQuantityChangedListener listener) {
        this.onQuantityChanged = listener;
    }

    public void setTitle(String title) {
        sTitle = title;
        this.title.setText(sTitle);
    }

    public void setSubTitle(String subTitle) {
        this.subTitle.setText(subTitle);
    }

    public void setQuantity (String quantity) {
        this.quantity.setText(quantity);
    }

    private void init () {
        quantity = itemView.findViewById(R.id.quantity);
        title = itemView.findViewById(R.id.title);
        subTitle = itemView.findViewById(R.id.subTitle);

        if (null != adapter) {
            quantity.setText(adapter.getMinQuantity());
        } else {
            quantity.setText("0");
        }

        quantity.setOnClickListener(v -> {
            final Dialog d = new Dialog(contextRequestListener.onCellContextRequest());
            d.setTitle(adapter.getTitle());
            d.setContentView(R.layout.number_picker_dialog);

            Button accept = d.findViewById(R.id.okButton);
            Button cancel = d.findViewById(R.id.cancelButton);
            TextView e = d.findViewById(R.id.numberPickerTitle);
            NumberPicker np = d.findViewById(R.id.numberPicker);

            e.setText(sTitle);

            np.setMaxValue(Integer.MAX_VALUE);
            if (adapter.isMaxQuantityRequired()) {
                np.setMaxValue(adapter.getMaxQuantity());
            }

            np.setMinValue(adapter.getMinQuantity());
            np.setWrapSelectorWheel(false);

            accept.setOnClickListener(v2 -> {
                int value = np.getValue();
                quantity.setText(String.valueOf(value));

                if (null != onQuantityChanged) {
                    onQuantityChanged.onQuantityChanged(value);
                }

                d.dismiss();
            });

            cancel.setOnClickListener( v2 -> d.dismiss());

            d.show();
        });
    }

    public interface QuantityCellAdapter {
        String getTitle();
        boolean isMaxQuantityRequired();
        int getMaxQuantity();
        int getMinQuantity();
        int getValue();
    }

    public interface OnQuantityChangedListener {
        void onQuantityChanged(int quantity);
    }
}
