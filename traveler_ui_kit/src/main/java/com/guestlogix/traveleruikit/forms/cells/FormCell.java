package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Generic cell which is going to be displayed in the form.
 * Provides general click listeners for the forms.
 * <p>
 * Notice:
 * Right now the cells can have at most one listener for a given listener type. This can create errors, if someone
 * decides to subscribe to the listeners on their own.
 */
public abstract class FormCell extends RecyclerView.ViewHolder {
    /**
     * Callback interface invoked whenever a user performs a click.
     */
    protected OnClickListener mClickListener;

    /**
     * Callback interface invoked whenever a user performs a long click.
     */
    protected OnLongClickListener mLongClickListener;

    /**
     * Callback interface invoked whenever a user changes the value.
     */
    protected OnTextChangedListener mTextChangesListener;

    /**
     * Index of the Holder Cell within the Form
     */
    protected int index;

    public FormCell(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * This listener is used to indicate that the user has clicked on the input.
     */
    public interface OnClickListener {

        /**
         * Is invoked whenever the user clicked on the input field.
         *
         * @param cell The {@link FormCell} object which was clicked.
         */
        void onClick(FormCell cell);
    }

    /**
     * This listener is used to indicate that the user has performed a long click on the input field.
     */
    public interface OnLongClickListener {

        /**
         * Is invoked whenever the user has performed a long click.
         *
         * @param cell The {@link FormCell} object associated with the long click.
         * @return true if the event was consumed.
         */
        boolean onLongClick(FormCell cell);
    }

    /**
     * This listener is used to indicate that the user has changed the value of the text.
     */
    public interface OnTextChangedListener {

        /**
         * Is invoked whenever the user changes the value of the input field.
         *
         * @param cell The {@link FormCell} object associated with the long click.
         * @param s    Current value.
         */
        void onTextChanged(FormCell cell, CharSequence s);
    }

    /**
     * Sets the callback click listener for the view.
     *
     * @param listener click listener.
     */
    public void setOnClickListener(OnClickListener listener) {
        this.mClickListener = listener;
    }

    /**
     * Sets the callback long click listener for the view.
     *
     * @param listener long click listener.
     */
    public void setOnLongClickListener(OnLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    /**
     * Sets the callback text change listener.
     *
     * @param listener text changed listener.
     */
    public void setOnTextChangedListener(OnTextChangedListener listener) {
        this.mTextChangesListener = listener;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
