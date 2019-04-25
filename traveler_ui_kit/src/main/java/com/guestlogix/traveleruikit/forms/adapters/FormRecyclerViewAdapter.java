package com.guestlogix.traveleruikit.forms.adapters;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;

public class FormRecyclerViewAdapter extends RecyclerView.Adapter<BaseCell>
        implements BaseCell.OnCellContextRequestListener {

    private FormMapper formMapper;
    private Context context;

    public FormRecyclerViewAdapter(@NonNull FormMapper formMapper, @NonNull Context context) {
        this.formMapper = formMapper;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseCell cell = formMapper.createViewHolder(parent, viewType);
        cell.setContextRequestListener(this);
        return cell;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseCell holder, int position) {
        formMapper.bindView(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return formMapper.getViewType(position);
    }

    @Override
    public int getItemCount() {
        return formMapper.getTotalCount();
    }

    @Override
    public Context onCellContextRequest() {
        return context;
    }

    public interface FormMapper {
        int getTotalCount();

        int getViewType(int position);

        BaseCell createViewHolder(ViewGroup parent, int type);

        void bindView(BaseCell cell, int position);
    }
}
