package com.guestlogix.traveleruikit.forms.adapters;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.forms.cells.FormCell;

public final class FormMappingAdapter extends RecyclerView.Adapter<FormCell> {
    private FormMapper formMapper;

    @NonNull
    @Override
    public FormCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null != formMapper) {
            FormCell cell = formMapper.createViewHolder(parent, viewType);
            return cell;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FormCell holder, int position) {
        if (null != formMapper) {
            formMapper.bindView(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (null != formMapper) {
            return formMapper.getViewType(position);
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return null != formMapper ? formMapper.getTotalCount() : 0;
    }

    public void setFormMapper(FormMapper formMapper) {
        this.formMapper = formMapper;
    }

    public interface FormMapper {
        int getTotalCount();
        int getViewType(int position);
        FormCell createViewHolder(ViewGroup parent, int type);
        void bindView(FormCell cell, int position);
    }
}
