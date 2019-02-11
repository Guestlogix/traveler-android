package com.guestlogix.traveleruikit.forms.models;

import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.cells.ButtonCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

/**
 * Form element which contains information to update a {@link BaseCell} with a button.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.listeners.OnFormElementClickListener}
 */
public class ButtonElement extends BaseElement {
    public final static FormType TYPE = FormType.BUTTON;

    public ButtonElement(String title) {
        super(title);
    }

    public ButtonElement() {}

    @Override
    public int getType() {
        return TYPE.getValue();
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void updateCell(BaseCell cell) {
        ButtonCell bCell = (ButtonCell) cell;

        bCell.setText(title);
        bCell.setButtonClickListener(clickListener);
    }

    private ButtonCell.OnButtonClickListener clickListener = () -> {
        if (null != onFormElementClickListener) {
            onFormElementClickListener.onFormElementClick(this);
        }
    };
}
