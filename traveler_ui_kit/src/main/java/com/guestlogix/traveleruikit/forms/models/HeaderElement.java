package com.guestlogix.traveleruikit.forms.models;

import com.guestlogix.traveleruikit.forms.cells.FormCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

public class HeaderElement extends BaseElement {
    public static final FormType TYPE = FormType.HEADER;

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
