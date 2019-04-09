package com.guestlogix.traveleruikit.forms;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.adapters.FormRecyclerViewAdapter;
import com.guestlogix.traveleruikit.forms.cells.*;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.FormModelType;
import com.guestlogix.traveleruikit.forms.models.HeaderFormModel;
import com.guestlogix.traveleruikit.forms.models.MessageFormModel;

import java.util.ArrayList;

public class Temp extends RecyclerView implements
        FormRecyclerViewAdapter.FormMapper,
        BaseCell.CellValueAdapter,
        BaseCell.OnCellClickListener,
        BaseCell.OnCellValueChangedListener,
        BaseCell.OnCellFocusChangeListener {

    // Interfaces.

    // Data
    private ArrayList<FormNode> nodes;
    // Adapters
    private DataSource dataSource;
    // Callbacks
    private FormFocusChangeListener formFocusChangeListener;
    private FormClickListener formClickListener;
    private FormValueChangedListener formValueChangedListener;

    public Temp(@NonNull Context context) {
        super(context);
        init();
    }

    public Temp(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Temp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;

        for (int i = 0; i < dataSource.getSectionCount(); i++) {
            FormHeader header = dataSource.getSectionHeader(i);
            if (header != null) {

                FormModel model = new HeaderFormModel(header.getTitle(), header.getSubtitle());
                FormNode node = new FormNode(i, -1, true);
                node.model = model;
                nodes.add(node);
            }

            int count = dataSource.getFieldCount(i);

            for (int j = 0; j < count; j++) {
                nodes.add(new FormNode(i, j, false));
            }
        }

        getAdapter().notifyDataSetChanged();
    }

    public void reload(int sectionId, int fieldId) {
        FormNode node = null;
        int i;
        for (i = 0; i < nodes.size() && node == null; i++) {
            FormNode t = nodes.get(i);

            if (t.sectionId == sectionId && t.fieldId == sectionId) {
                node = t;
            }
        }

        if (node != null) {
            node.model = null;
            getAdapter().notifyItemChanged(i);
        }
    }

    @Override
    public int getTotalCount() {
        return nodes.size();
    }

    @Override
    public int getViewType(int position) {
        FormNode node = nodes.get(position);

        // JIT.
        if (node.model == null) {
            node.model = dataSource.getModel(node.sectionId, node.fieldId);
        }

        return node.model.getType();
    }

    @Override
    public BaseCell createViewHolder(ViewGroup parent, int type) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(getContext());

        switch (FormModelType.valueOf(type)) {
            case QUANTITY:
                view = inflater.inflate(FormLayout.QUANTITY_LAYOUT, parent, false);
                return new QuantityCell(view);
            case DATE:
                view = inflater.inflate(FormLayout.DATE_LAYOUT, parent, false);
                return new DateCell(view);
            case SPINNER:
                view = inflater.inflate(FormLayout.SPINNER_LAYOUT, parent, false);
                return new SpinnerCell(view);
            case MESSAGE:
                view = inflater.inflate(FormLayout.MESSAGE_LAYOUT, parent, false);
                return new MessageCell(view);
            case BUTTON:
                view = inflater.inflate(FormLayout.BUTTON_LAYOUT, parent, false);
                return new ButtonCell(view);
            case TEXT:
                view = inflater.inflate(FormLayout.TEXT_LAYOUT, parent, false);
                return new TextCell(view);
            case HEADER:
                view = inflater.inflate(FormLayout.HEADER_LAYOUT, parent, false);
                return new HeaderCell(view);
            default:
                return null;
        }
    }

    @Override
    public void bindView(BaseCell cell, int position) {
        FormNode node = nodes.get(position);

        // JIT node rendering.
        if (node.model == null) {
            node.model = dataSource.getModel(node.sectionId, node.fieldId);
        }

        // Unregister previous listeners.
        cell.setOnCellValueChangedListener(null);
        cell.setOnCellFocusChangeListener(null);
        cell.setOnCellClickListener(null);

        cell.setCellValueAdapter(this);
        cell.bindWithModel(node.model);

        cell.setOnCellClickListener(this);
        cell.setOnCellFocusChangeListener(this);
        cell.setOnCellValueChangedListener(this);

        /*

            Checks if this form needs a message.

            If it does, checks whether a message already exists or not and adds/updates ir accordingly.
            If it does not, but has a message. removes the next element in the list.

            A node has a message if the method getMessage from the DataSource returns a non null object.

         */
        FormMessage message = dataSource.getMessage(node.sectionId, node.fieldId);

        if (message != null) {
            FormNode msgNode;

            if (node.hasMessage) {
                msgNode = nodes.get(position + 1);
            } else {
                msgNode = new FormNode(-1, -1, false);
            }

            msgNode.model = new MessageFormModel(message);

            if (node.hasMessage) {
                getAdapter().notifyItemChanged(position + 1);
            } else {
                nodes.add(position + 1, msgNode);
                getAdapter().notifyItemInserted(position + 1);
            }

            node.hasMessage = true;
        } else if (node.hasMessage) {
            nodes.remove(position + 1);
            node.hasMessage = false;
            getAdapter().notifyItemRemoved(position);
        }
    }

    @Override
    public Object getCellValue(BaseCell cell) {
        int position = cell.getAdapterPosition();
        FormNode node = nodes.get(position);

        return dataSource.getValue(node.sectionId, node.fieldId);
    }

    @Override
    public void onCellValueChanged(BaseCell caller, Object value) {
        if (formValueChangedListener != null) {
            FormNode node = nodes.get(caller.getAdapterPosition());
            formValueChangedListener.onFormValueChanged(node.sectionId, node.fieldId, value);
        }
    }

    @Override
    public void onCellClick(BaseCell caller) {
        if (formClickListener != null) {
            FormNode node = nodes.get(caller.getAdapterPosition());
            formClickListener.onFormClick(node.sectionId, node.fieldId);
        }
    }

    @Override
    public void onCellFocusChange(BaseCell caller, boolean hasFocus) {
        if (formFocusChangeListener != null) {
            FormNode node = nodes.get(caller.getAdapterPosition());
            formFocusChangeListener.onFormFocusChange(node.sectionId, node.fieldId, hasFocus);
        }
    }

    public void setFormFocusChangeListener(FormFocusChangeListener formFocusChangeListener) {
        this.formFocusChangeListener = formFocusChangeListener;
    }

    public void setFormClickListener(FormClickListener formClickListener) {
        this.formClickListener = formClickListener;
    }

    public void setFormValueChangedListener(FormValueChangedListener formValueChangedListener) {
        this.formValueChangedListener = formValueChangedListener;
    }

    private void init() {
        FormRecyclerViewAdapter adapter = new FormRecyclerViewAdapter(this);
        adapter.setContextRequestListener(this::getContext);
        setAdapter(adapter);
        nodes = new ArrayList<>();
    }

    public interface FormValueChangedListener {
        void onFormValueChanged(int sectionId, int fieldId, Object value);
    }

    public interface FormClickListener {
        void onFormClick(int sectionId, int fieldId);
    }

    public interface FormFocusChangeListener {
        void onFormFocusChange(int sectionId, int fieldId, boolean hasFocus);
    }

    public interface DataSource {
        int getSectionCount();

        int getFieldCount(int sectionId);

        @NonNull
        FormModel getModel(int sectionId, int fieldId);

        @Nullable
        FormHeader getSectionHeader(int sectionId);

        @Nullable
        FormMessage getMessage(int sectionId, int fieldId);

        @Nullable
        Object getValue(int sectionId, int fieldId);
    }

    private static class FormLayout {
        @LayoutRes
        static final int TEXT_LAYOUT = R.layout.form_text_input;
        @LayoutRes
        static final int BUTTON_LAYOUT = R.layout.form_button;
        @LayoutRes
        static final int HEADER_LAYOUT = R.layout.form_header;
        @LayoutRes
        static final int QUANTITY_LAYOUT = R.layout.form_quantity;
        @LayoutRes
        static final int SPINNER_LAYOUT = R.layout.form_spinner;
        @LayoutRes
        static final int MESSAGE_LAYOUT = R.layout.form_message_cell;
        @LayoutRes
        static final int DATE_LAYOUT = R.layout.form_date;
    }

    private class FormNode {
        int sectionId;
        int fieldId;
        boolean hasMessage;
        boolean isHeader;

        @Nullable
        FormModel model;

        FormNode(int sectionId, int fieldId, boolean isHeader) {
            this.sectionId = sectionId;
            this.fieldId = fieldId;
            this.isHeader = isHeader;
            this.hasMessage = false;
        }
    }
}
