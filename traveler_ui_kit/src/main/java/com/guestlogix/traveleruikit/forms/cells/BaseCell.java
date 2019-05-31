package com.guestlogix.traveleruikit.forms.cells;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.models.FormModel;

public abstract class BaseCell extends RecyclerView.ViewHolder {

    OnCellContextRequestListener contextRequestListener;

    CellValueAdapter cellValueAdapter;

    CellEventsListener cellEventsListener;

    BaseCell(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * Binds a form cell with a matching FormModel.
     *
     * @param model description of the cell.
     */
    public abstract void bindWithModel(@NonNull FormModel model);

    public abstract void setMessage(@Nullable FormMessage message);

    /**
     * Registers a callback to be invoked whenever an event happens in a cell.
     *
     * @param l The callback that will be invoked
     */
    public void setCellEventsListener(CellEventsListener l) {
        this.cellEventsListener = l;
    }

    /**
     * Subscribes to context request events.
     *
     * @param l handles context request events
     */
    public void setContextRequestListener(OnCellContextRequestListener l) {
        this.contextRequestListener = l;
    }

    /**
     * Adds an adapter which gets the cell value.
     *
     * @param cellValueAdapter adapter to be used by a cell
     */
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


    public interface CellEventsListener {
        /**
         * Is invoked whenever the focus was changed in a BaseCell.
         * Up to individual cells to use this callback.
         *
         * @param caller   Cell which invoked the event.
         * @param hasFocus Whether the cell has focus right now.
         */
        void onCellFocusChange(BaseCell caller, boolean hasFocus);

        /**
         * Is invoked whenever a BaseCell is clicked.
         *
         * @param caller Cell which invoked the event.
         */
        void onCellClick(BaseCell caller);

        /**
         * Is invoked whenever a BaseCell has its value changed.
         *
         * @param caller Cell which invoked the event.
         * @param value  new value of the cell, can be anything based on th cell type.
         */
        void onCellValueChanged(BaseCell caller, Object value);
    }

    /**
     * Callback used to request values for the cell. Might be used multiple times per cell binding.
     */
    public interface CellValueAdapter {
        /**
         * Returns the value for the current cell.
         *
         * @param cell where to get value for
         * @return value of the cell
         */
        Object getCellValue(BaseCell cell);
    }
}
