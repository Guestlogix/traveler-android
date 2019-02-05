package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
         * @param v The {@link Input} object which was clicked.
         */
        void onClick(View v);
    }

    /**
     * This listener is used to indicate that the user has performed a long click on the input field.
     */
    public interface OnLongClickListener {

        /**
         * Is invoked whenever the user has performed a long click.
         *
         * @param v The {@link Input} object associated with the long click.
         * @return true if the event was consumed.
         */
        boolean onLongClick(View v);
    }

    /**
     * This listener is used to indicate that the user has changed the value of the text.
     */
    public interface OnTextChangedListener {

        /**
         * Is invoked whenever the user changes the value of the input field.
         *
         * @param s      Current value.
         * @param start  Start position of characters that have been changed.
         * @param before Length of value before the change.
         * @param count  Amount of characters which were changed.
         */
        void onTextChanged(CharSequence s, int start, int before, int count);
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
}
