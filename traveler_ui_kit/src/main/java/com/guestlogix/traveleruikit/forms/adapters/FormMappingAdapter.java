package com.guestlogix.traveleruikit.forms.adapters;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.forms.cells.FormCell;
import com.guestlogix.traveleruikit.forms.utilities.FormBuilder;

public class FormMappingAdapter extends RecyclerView.Adapter<FormCell> {
    private FormMapper formMapper;

    public FormMappingAdapter(@NonNull FormMapper formMapper) {
        this.formMapper = formMapper;
    }

    @NonNull
    @Override
    public FormCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FormCell cell = formMapper.createViewHolder(parent, viewType);
        return cell;
    }

    @Override
    public void onBindViewHolder(@NonNull FormCell holder, int position) {
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

    public interface FormMapper {
        int getTotalCount();

        int getViewType(int position);

        FormCell createViewHolder(ViewGroup parent, int type);

        void bindView(FormCell cell, int position);
    }
}
