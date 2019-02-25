package com.guestlogix.traveleruikit.forms.cells;

import android.app.Dialog;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.traveleruikit.R;

/**
 * Cell which contains a quantity picker.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.cells.BaseCell.OnCellValueChangedListener}
 */
public class QuantityCell extends BaseCell {
    private TextView quantity;
    private TextView title;
    private TextView subTitle;

    private QuantityCellAdapter adapter;

    public QuantityCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    @Override
    public void reload() {
        quantity.setText("");
    }

    public void setAdapter(QuantityCellAdapter adapter) {
        this.adapter = adapter;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setSubtitle(String subTitle) {
        this.subTitle.setText(subTitle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.subTitle.setText(Html.fromHtml(subTitle, Html.FROM_HTML_MODE_COMPACT).toString());
        } else {
            this.subTitle.setText(Html.fromHtml(subTitle).toString());
        }
    }

    public void setQuantity(String quantity) {
        this.quantity.setText(quantity);
    }

    private void init() {
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

            e.setText(adapter.getTitle());

            np.setMaxValue(Integer.MAX_VALUE);
            if (adapter.getMaxQuantity() != null) {
                np.setMaxValue(adapter.getMaxQuantity());
            }

            np.setMinValue(adapter.getMinQuantity());
            np.setWrapSelectorWheel(false);

            np.setValue(adapter.getValue());

            accept.setOnClickListener(v2 -> {
                Integer value = np.getValue();
                quantity.setText(String.valueOf(value));

                if (null != onCellValueChangedListener) {
                    onCellValueChangedListener.onCellValueChanged(this, value);
                }

                d.dismiss();
            });

            cancel.setOnClickListener(v2 -> d.dismiss());

            d.show();
        });
    }

    public interface QuantityCellAdapter {
        String getTitle();
        @Nullable Integer getMaxQuantity();
        @NonNull Integer getMinQuantity();
        @NonNull Integer getValue();
    }
}
