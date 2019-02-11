package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.cells.ButtonCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

public class ButtonElement extends BaseElement {
    public final static FormType TYPE = FormType.BUTTON;
    private String title;

    public ButtonElement(String title) {
        this.title = title;
    }

    public ButtonElement() {}

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
        ButtonCell bCell = (ButtonCell) cell;

        bCell.setText(title);
        bCell.setButtonClickListener(clickListener);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    ButtonCell.OnButtonClickListener clickListener = () -> {
        if (null != onFormElementClickListener) {
            onFormElementClickListener.onFormElementClick(this);
        }
    };
}
