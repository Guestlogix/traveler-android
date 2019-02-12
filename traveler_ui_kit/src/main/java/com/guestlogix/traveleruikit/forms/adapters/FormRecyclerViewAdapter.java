package com.guestlogix.traveleruikit.forms.adapters;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;

public class FormRecyclerViewAdapter extends RecyclerView.Adapter<BaseCell> {
    private FormMapper formMapper;
    private OnFormContextRequestListener contextRequestListener;

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
        formMapper.bindView(holder, position);
        holder.setIndex(position);
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

    public interface FormMapper {
        int getTotalCount();

        int getViewType(int position);

        BaseCell createViewHolder(ViewGroup parent, int type);

        void bindView(BaseCell cell, int position);
    }

    public interface OnFormContextRequestListener {
        Context onFormContextRequest();
    }

    private BaseCell.OnCellContextRequestListener contextRequestHandler = () -> null != contextRequestListener ? contextRequestListener.onFormContextRequest() : null;
}
