package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.FormCell;
import com.guestlogix.traveleruikit.forms.cells.QuantityCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

import java.util.Queue;

public class QuantityElement extends BaseElement {
    public static final FormType TYPE = FormType.QUANTITY;
    private String value;
    private boolean isMaxRequired = false;
    private int maxRequired;
    private int minRequired;

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
        qCell.setmOnValueChangedLister(valueChangedListener);
    }

    public String getValue() {
        return value;
    }

    public boolean isMaxRequired() {
        return isMaxRequired;
    }

    public void setMaxRequired(boolean maxRequired) {
        isMaxRequired = maxRequired;
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

    QuantityCell.OnValueChangedListener valueChangedListener = newValue -> value = newValue;
    QuantityCell.QuantityCellAdapter adapter = new QuantityCell.QuantityCellAdapter() {
        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public boolean isMaxQuantityRequired() {
            return false;
        }

        @Override
        public int getMaxQuantity() {
            return 0;
        }

        @Override
        public int getMinQuantity() {
            return 0;
        }
    };
}
