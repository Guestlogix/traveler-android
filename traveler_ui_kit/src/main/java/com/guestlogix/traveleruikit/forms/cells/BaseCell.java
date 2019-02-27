package com.guestlogix.traveleruikit.forms.cells;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Generic cell which is going to be displayed in the form. Cells are displayed in a flat manner in the recycler view.
 * <p>
 * Notice:
 * Right now the cells can have at most one listener for a given listener type. This can create errors, if someone
 * decides to subscribe to the listeners on their own.
 * <p>
 * Feb 6th, 2019:
 * We need to make sure that each Cell is only tied to a specific view which contains the correct views. So far all
 * the cells require specific ids to operate correctly. Right now there is no mechanism stopping someone from
 * creating a ViewHolder with an incompatible view type. This will cause the app to crash at runtime.
 */
public abstract class BaseCell extends RecyclerView.ViewHolder {

    /**
     * Index of the Holder Cell within the Form
     */
    private int index;

    OnCellContextRequestListener contextRequestListener;

    OnCellValueChangedListener onCellValueChangedListener;

    OnCellClickListener onCellClickListener;

    OnCellFocusChangeListener onCellFocusChangeListener;

    BaseCell(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * Index of the ViewHolder within the form.
     *
     * @return int
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void hide() {
        itemView.setVisibility(View.GONE);
    }

    public void show() {
        itemView.setVisibility(View.VISIBLE);
    }

    public void setError(String error) {

    }

    /**
     * Signals the cell that it needs to be reloaded to a fresh state.
     */
    public abstract void reload();

    /**
     * Subscribes to context request events.
     *
     * @param contextRequestListener handles context request events.
     */
    public void setContextRequestListener(OnCellContextRequestListener contextRequestListener) {
        this.contextRequestListener = contextRequestListener;
    }

    /**
     * Subscribes to value change events.
     *
     * @param onCellValueChangedListener callback for value change events.
     */
    public void setOnCellValueChangedListener(OnCellValueChangedListener onCellValueChangedListener) {
        this.onCellValueChangedListener = onCellValueChangedListener;
    }

    /**
     * Subscribes to click events.
     *
     * @param onCellClickListener callback for click events.
     */
    public void setOnCellClickListener(OnCellClickListener onCellClickListener) {
        this.onCellClickListener = onCellClickListener;
    }

    /**
     * Subscribes to focus change events.
     *
     * @param onCellFocusChangeListener callback for focus change events.
     */
    public void setOnCellFocusChangeListener(OnCellFocusChangeListener onCellFocusChangeListener) {
        this.onCellFocusChangeListener = onCellFocusChangeListener;
    }

    /**
     * Listener for events where the cell needs the activity context to function.
     */
    public interface OnCellContextRequestListener {
        /**
         * Is invoked whenever a BaseCell needs the activity context.
         *
         * @return The activity context.
         */
        Context onCellContextRequest();
    }

    /**
     * Listener for events where the value of the cell changed.
     * It is the responsibility of the individual cells to use this listener.
     */
    public interface OnCellValueChangedListener {
        /**
         * Is invoked whenever a BaseCell has its value changed.
         *
         * @param caller Cell which invoked the event.
         * @param value  new value of the cell, can be anything based on th cell type.
         */
        void onCellValueChanged(BaseCell caller, Object value);
    }

    /**
     * Listener for click events.
     */
    public interface OnCellClickListener {
        /**
         * Is invoked whenever a BaseCell is clicked.
         *
         * @param caller Cell which invoked the event.
         */
        void onCellClick(BaseCell caller);
    }

    /**
     * Listener for focus change events.
     */
    public interface OnCellFocusChangeListener {
        /**
         * Is invoked whenever the focus was changed in a BaseCell.
         * Up to individual cells to use this callback.
         *
         * @param caller   Cell which invoked the event.
         * @param hasFocus Whether the cell has focus right now.
         */
        void onCellFocusChange(BaseCell caller, boolean hasFocus);
    }
}
