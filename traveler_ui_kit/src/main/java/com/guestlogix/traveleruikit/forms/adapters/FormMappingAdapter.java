package com.guestlogix.traveleruikit.forms.adapters;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.forms.cells.FormCell;

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
        holder.setOnClickListener(onClickListener);
        holder.setOnLongClickListener(onLongClickListener);
        holder.setOnTextChangedListener(onTextChangedListener);
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

        void onClick(int position);

        boolean onLongClick(int position);

        void onTextChanged(int position, CharSequence s);
    }

    FormCell.OnClickListener onClickListener = new FormCell.OnClickListener() {
        @Override
        public void onClick(FormCell cell) {
            if (null != formMapper) {
                formMapper.onClick(cell.getIndex());
            }
        }
    };

    FormCell.OnLongClickListener onLongClickListener = new FormCell.OnLongClickListener() {
        @Override
        public boolean onLongClick(FormCell cell) {
            return null != formMapper && formMapper.onLongClick(cell.getIndex());
        }
    };

    FormCell.OnTextChangedListener onTextChangedListener = new FormCell.OnTextChangedListener() {
        @Override
        public void onTextChanged(FormCell cell, CharSequence s) {
            if (null != formMapper) {
                formMapper.onTextChanged(cell.getIndex(), s);
            }
        }
    };
}
