package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.cells.SpinnerCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

import java.util.List;

/**
 * Form element which holds information to update a {@link BaseCell} with a spinner.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.listeners.OnFormElementValueChangedListener}
 */
public class SpinnerElement extends BaseElement {
    public static final FormType TYPE = FormType.SPINNER;

    private List<String> options;
    private Integer value;

    public SpinnerElement() {
        super();
    }

    public SpinnerElement(String title, String subtitle, List<String> options) {
        super(title, subtitle);

        this.options = options;
    }

    public SpinnerElement(String title, String subtitle, List<String> options, int defaultValue) {
        super(title, subtitle);

        this.options = options;
        this.value = defaultValue;
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
        SpinnerCell sCell = (SpinnerCell) cell;

        sCell.setTitle(title);
        sCell.setSubtitle(subtitle);
        sCell.setSpinnerCellCallback(listener);

        if (null != value) {
            sCell.setOptions(options, value);
        } else {
            sCell.setOptions(options);
        }
    }

    /**
     * Returns the current value selected in the list.
     *
     * @return relative position of the element within the list.
     */
    public int getValue() {
        return value;
    }

    private SpinnerCell.SpinnerCellCallback listener = (pos) -> {
        value = pos;

        if (null != onFormElementValueChangedListener) {
            onFormElementValueChangedListener.onValueChanged(this);
        }
    };
}
