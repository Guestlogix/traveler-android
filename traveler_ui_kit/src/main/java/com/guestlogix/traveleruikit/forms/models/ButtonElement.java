package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.ButtonCell;
import com.guestlogix.traveleruikit.forms.cells.FormCell;
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
    public FormType getType() {
        return TYPE;
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void updateCell(FormCell cell) {
        ButtonCell bCell = (ButtonCell) cell;

        bCell.setText(title);
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
        // TODO: Add click callback.
    };
}
