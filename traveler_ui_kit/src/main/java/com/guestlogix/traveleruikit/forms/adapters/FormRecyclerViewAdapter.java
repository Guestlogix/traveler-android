package com.guestlogix.traveleruikit.forms.adapters;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;

public class FormRecyclerViewAdapter extends RecyclerView.Adapter<BaseCell> {
    private FormMapper formMapper;
    private OnFormContextRequestListener contextRequestListener;
    private CellEventsListener cellEventsListener;

    public FormRecyclerViewAdapter(@NonNull FormMapper formMapper) {
        this.formMapper = formMapper;
    }

    @NonNull
    @Override
    public BaseCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseCell cell = formMapper.createViewHolder(parent, viewType);
        cell.setContextRequestListener(contextRequestHandler);
        return cell;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseCell holder, int position) {
        holder.setOnCellClickListener(null);
        holder.setOnCellFocusChangeListener(null);
        holder.setOnCellValueChangedListener(null);

        formMapper.bindView(holder, position);

        holder.setOnCellClickListener(this::onCellClick);
        holder.setOnCellFocusChangeListener(this::onCellFocusChange);
        holder.setOnCellValueChangedListener(this::onCellValueChanged);
    }

    @Override
    public int getItemViewType(int position) {
        return formMapper.getViewType(position);
    }

    @Override
    public int getItemCount() {
        return formMapper.getTotalCount();
    }

    public void setContextRequestListener(OnFormContextRequestListener contextRequestListener) {
        this.contextRequestListener = contextRequestListener;
    }

    public void setCellEventsListener(CellEventsListener cellEventsListener) {
        this.cellEventsListener = cellEventsListener;
    }

    public interface FormMapper {
        int getTotalCount();
        int getViewType(int position);
        BaseCell createViewHolder(ViewGroup parent, int type);
        void bindView(BaseCell cell, int position);
    }

    public interface CellEventsListener {
        void onValueChanged(int pos, Object value);
        void onFocusChanged(int pos, boolean hasFocus);
        void onClick(int pos);
    }

    public interface OnFormContextRequestListener {
        Context onFormContextRequest();
    }

    private void onCellClick(BaseCell cell) {
        int pos = cell.getAdapterPosition();

        if (cellEventsListener != null) {
            cellEventsListener.onClick(pos);
        }
    }

    private void onCellFocusChange(BaseCell cell, boolean hasFocus) {
        int pos = cell.getAdapterPosition();

        if (cellEventsListener != null) {
            cellEventsListener.onFocusChanged(pos, hasFocus);
        }
    }

    private void onCellValueChanged(BaseCell cell, Object value) {
        int pos = cell.getAdapterPosition();

        if (cellEventsListener != null) {
            cellEventsListener.onValueChanged(pos, value);
        }
    }

    private BaseCell.OnCellContextRequestListener contextRequestHandler = () -> null != contextRequestListener ?
            contextRequestListener.onFormContextRequest() : null;
}
