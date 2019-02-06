package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.FormCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

public class TextElement extends BaseElement {
    public final static FormType TYPE = FormType.TEXT;

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
