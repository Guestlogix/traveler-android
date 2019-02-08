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
public abstract class FormCell extends RecyclerView.ViewHolder {

    /**
     * Index of the Holder Cell within the Form
     */
    protected int index;

    protected OnCellContextRequestListener contextRequestListener;

    public FormCell(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * Index of the ViewHolder within the form.
     * @return int
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public abstract void reload();

    public void setContextRequestListener(OnCellContextRequestListener contextRequestListener) {
        this.contextRequestListener = contextRequestListener;
    }

    public interface OnCellContextRequestListener {
        Context onCellContextRequest();
    }
}
