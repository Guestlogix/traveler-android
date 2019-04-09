package com.guestlogix.traveleruikit.forms.cells;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.forms.models.FormModel;

public abstract class BaseCell extends RecyclerView.ViewHolder {

    OnCellContextRequestListener contextRequestListener;

    OnCellValueChangedListener onCellValueChangedListener;

    OnCellClickListener onCellClickListener;

    OnCellFocusChangeListener onCellFocusChangeListener;

    CellValueAdapter cellValueAdapter;

    BaseCell(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindWithModel(@NonNull FormModel model);

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

    public void setCellValueAdapter(CellValueAdapter cellValueAdapter) {
        this.cellValueAdapter = cellValueAdapter;
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

    public interface CellValueAdapter {
        Object getCellValue(BaseCell cell);
    }
}
