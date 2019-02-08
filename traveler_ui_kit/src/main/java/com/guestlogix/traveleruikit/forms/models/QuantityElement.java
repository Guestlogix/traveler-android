package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.FormCell;
import com.guestlogix.traveleruikit.forms.cells.QuantityCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

public class QuantityElement extends BaseElement {
    public static final FormType TYPE = FormType.QUANTITY;
    private int value;
    private int maxRequired;
    private int minRequired;
    private String subtitle;

    public QuantityElement(String title, String subtitle, int minRequired, int maxRequired) {
        super (title);

        this.subtitle = subtitle;
        this.minRequired = minRequired;
        this.maxRequired = maxRequired;
        value = minRequired;
    }

    public QuantityElement(String title, String subtitle, int minRequired) {
        super(title);
        this.subtitle = subtitle;
        this.minRequired = minRequired;
        this.maxRequired = -1;
        value = minRequired;
    }

    public QuantityElement () {
        minRequired = 0;
        maxRequired = -1;
        value = minRequired;
    }

    @NonNull
    @Override
    public FormType getType() {
        return TYPE;
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void updateCell(FormCell cell) {
        QuantityCell qCell = (QuantityCell) cell;

        qCell.setAdapter(adapter);
        qCell.setOnQuantityChangedListener(onQuantityChanged);
        qCell.setTitle(title);
        qCell.setSubTitle(subtitle);
        qCell.setQuantity(String.valueOf(value));
    }

    public int getValue() {
        return value;
    }

    public int getMaxRequired() {
        return maxRequired;
    }

    public void setMaxRequired(int maxRequired) {
        this.maxRequired = maxRequired;
    }

    public int getMinRequired() {
        return minRequired;
    }

    public void setMinRequired(int minRequired) {
        this.minRequired = minRequired;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    // How to initialize the quantity picker.
    QuantityCell.QuantityCellAdapter adapter = new QuantityCell.QuantityCellAdapter() {
        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public boolean isMaxQuantityRequired() {
            return isMaxRequired();
        }

        @Override
        public int getMaxQuantity() {
            return maxRequired;
        }

        @Override
        public int getMinQuantity() {
            return minRequired;
        }
    };

    public boolean isMaxRequired() {
        return maxRequired >= 0;
    }

    // Signal value changed.
    QuantityCell.OnQuantityChangedListener onQuantityChanged = (val) -> {
        value = val;

        if (null != onFormElementValueChangedListener) {
            onFormElementValueChangedListener.onValueChanged(this);
        }
    };
}
