package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.FormCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

public abstract class BaseElement {
    protected String title;

    private int index;

    @NonNull
    public abstract FormType getType();

    /**
     * Sets the type for any user defined elements.
     * This function should only be called by the {@link com.guestlogix.traveleruikit.forms.utilities.FormBuilder} class!
     *
     * @param type type
     */
    public abstract void setType(int type);

    public abstract void updateCell (FormCell cell);

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
