package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.cells.HeaderCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

public class HeaderElement extends BaseElement {
    public static final FormType TYPE = FormType.HEADER;

    private String subtitle;

    public HeaderElement(String title, String subtitle) {
        super(title);
        this.subtitle = subtitle;
    }

    public HeaderElement(String title) {
        super(title);
    }

    public HeaderElement() {}

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
        HeaderCell hCell = (HeaderCell) cell;

        hCell.setTitle(title);
        hCell.setSubTitle(subtitle);
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
