package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.listeners.OnFormElementClickListener;
import com.guestlogix.traveleruikit.forms.listeners.OnFormElementValueChangedListener;

/**
 * Data holder class used to update any {@link BaseCell} classes with the appropriate values.
 * <p>
 * Usually each specific {@link BaseElement} class has a specific {@link BaseCell} class coupled with it so updating the
 * view holder is trivial.
 */
public abstract class BaseElement {
    protected String title;
    protected String subtitle;

    protected State state;
    protected String errorMessage;
    protected String infoMessage;

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
        state = State.DEFAULT;
    }

    public BaseElement(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        state = State.DEFAULT;
    }

    public BaseElement() {
        state = State.DEFAULT;
    }

    @NonNull
    public abstract int getType();

    public void reload() {
        this.state = State.DEFAULT;
    }

    /**
     * Sets the type for any user defined elements.
     * This function should only be called by the {@link com.guestlogix.traveleruikit.forms.utilities.FormBuilder} class!
     *
     * @param type type
     */
    public abstract void setType(int type);

    /**
     * Used to update a specific cell. Can assume that the given cell is of a matching type.
     *
     * @param cell View Holder to update.
     */
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

    public void setState(State state) {
        this.state = state;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
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

    public enum State {
        DEFAULT, ERROR, INFO
    }
}
