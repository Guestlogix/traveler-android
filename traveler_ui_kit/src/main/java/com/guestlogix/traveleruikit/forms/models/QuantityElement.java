package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.cells.QuantityCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

/**
 * Form element which contains information required to update a {@link BaseCell} with a quantity.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.listeners.OnFormElementValueChangedListener}
 */
public class QuantityElement extends BaseElement {
    public static final FormType TYPE = FormType.QUANTITY;
    private Integer value;
    private int maxRequired;
    private int minRequired;

    public QuantityElement(String title, String subtitle, int minRequired, int maxRequired) {
        super(title, subtitle);

        this.minRequired = minRequired;
        this.maxRequired = maxRequired;
        value = minRequired;
    }

    public QuantityElement(String title, String subtitle, int minRequired) {
        super(title, subtitle);

        this.minRequired = minRequired;
        this.maxRequired = -1;
        value = minRequired;
    }

    public QuantityElement() {
        minRequired = 0;
        maxRequired = -1;
        value = minRequired;
    }

    @NonNull
    @Override
    public int getType() {
        return TYPE.getValue();
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void updateCell(BaseCell cell) {
        QuantityCell qCell = (QuantityCell) cell;

        qCell.setAdapter(adapter);
        qCell.setOnQuantityChangedListener(onQuantityChanged);
        qCell.setTitle(title);
        qCell.setSubTitle(subtitle);

        if (null != value) {
            qCell.setQuantity(value.toString());
        } else {
            qCell.setQuantity(String.valueOf(minRequired));
        }
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

        @Override
        public int getValue() {
            return value == null ? minRequired : value;
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
