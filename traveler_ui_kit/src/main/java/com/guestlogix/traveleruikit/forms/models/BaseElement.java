package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.listeners.OnFormElementClickListener;
import com.guestlogix.traveleruikit.forms.listeners.OnFormElementValueChangedListener;

public abstract class BaseElement {
    protected String title;
    protected String subtitle;

    private int index;

    /**
     * Callback interface for value change events. Up to extending classes to implement.
     */
    protected OnFormElementValueChangedListener onFormElementValueChangedListener;

    /**
     * Callback for click events on elements of the form. Up to individual classes to implement.
     */
    protected OnFormElementClickListener onFormElementClickListener;

    public BaseElement(String title) {
        this.title = title;
    }

    public BaseElement(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public BaseElement() {
    }

    @NonNull
    public abstract int getType();

    /**
     * Sets the type for any user defined elements.
     * This function should only be called by the {@link com.guestlogix.traveleruikit.forms.utilities.FormBuilder} class!
     *
     * @param type type
     */
    public abstract void setType(int type);

    public abstract void updateCell(BaseCell cell);

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Sets the callback interface for value change events. Does not guarantee that all extending elements provide click events
     * or implement this callback interface.
     *
     * @param onFormElementValueChangedListener callback for value change events.
     */
    public void setOnFormElementValueChangedListener(OnFormElementValueChangedListener onFormElementValueChangedListener) {
        this.onFormElementValueChangedListener = onFormElementValueChangedListener;
    }

    /**
     * Sets the callback interface for click events. Does not guarantee that all extending elements provide click events
     * or implement this callback interface.
     *
     * @param onFormElementClickListener callback for click events.
     */
    public void setOnFormElementClickListener(OnFormElementClickListener onFormElementClickListener) {
        this.onFormElementClickListener = onFormElementClickListener;
    }
}
