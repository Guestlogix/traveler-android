package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.ButtonCell;
import com.guestlogix.traveleruikit.forms.cells.FormCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

public class ButtonElement extends BaseElement {
    public final static FormType TYPE = FormType.BUTTON;

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
    }
}
