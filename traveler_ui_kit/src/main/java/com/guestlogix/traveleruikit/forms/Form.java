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

import java.util.ArrayList;

/**
 * Wrapper around recycler view used to render a form. Transforms a two dimensional array of questions/question groups
 * into a single dimensional list of views.
 */
public class Form extends RecyclerView implements
        FormRecyclerViewAdapter.FormMapper,
        BaseCell.CellValueAdapter,
        BaseCell.OnCellClickListener,
        BaseCell.OnCellValueChangedListener,
        BaseCell.OnCellFocusChangeListener {

    // Data
    private ArrayList<FormNode> nodes;

    // Adapters
    private DataSource dataSource;

    // Callbacks
    /**
     * Listener used to dispatch focus change events.
     */
    private FormFocusChangeListener formFocusChangeListener;

    /**
     * Listener used to dispatch click events.
     */
    private FormClickListener formClickListener;

    /**
     * Listener used to dispatch value change events.
     */
    private FormValueChangedListener formValueChangedListener;

    public Form(@NonNull Context context) {
        super(context);
        init();
    }

    public Form(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Form(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
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

    /**
     * Rebinds a specific item in the form. This can result in a structural change in the form.
     *
     * @param sectionId Section index
     * @param fieldId   Field index
     */
    public void reload(int sectionId, int fieldId) {
        Integer position = null;

        for (int i = 0; i < nodes.size() && position == null; i++) {
            FormNode t = nodes.get(i);

            if (t.sectionId == sectionId && t.fieldId == fieldId) {
                position = i;
            }
        }

        if (position != null) {
            FormNode node = nodes.get(position);
            node.model = null;
            getAdapter().notifyItemChanged(position);
        }
    }

    /**
     * Smooth scrolls to a given position.
     *
     * @param sectionId Section index
     * @param fieldId   Field index
     */
    public void smoothScrollToPosition(int sectionId, int fieldId) {
        Integer position = null;

        for (int i = 0; i < nodes.size() && position == null; i++) {
            FormNode node = nodes.get(i);

            if (node.sectionId == sectionId && node.fieldId == fieldId) {
                position = i;
            }
        }

        if (position != null) {
            getLayoutManager().smoothScrollToPosition(this, null, position);
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

        FormMessage message = dataSource.getMessage(node.sectionId, node.fieldId);
        cell.setMessage(message);
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

    /**
     * Registers a callback to be invoked whenever the form focus changes.
     *
     * @param l The callback that will be run
     */
    public void setFormFocusChangeListener(FormFocusChangeListener l) {
        this.formFocusChangeListener = l;
    }

    /**
     * Registers a callback to be invoked whenever the form is clicked.
     *
     * @param l The callback that will be run
     */
    public void setFormClickListener(FormClickListener l) {
        this.formClickListener = l;
    }

    /**
     * Register a callback to be invoked whenever a value changes in the form.
     *
     * @param l The callback that will be run
     */
    public void setFormValueChangedListener(FormValueChangedListener l) {
        this.formValueChangedListener = l;
    }

    private void init() {
        FormRecyclerViewAdapter adapter = new FormRecyclerViewAdapter(this);
        adapter.setContextRequestListener(this::getContext);
        setAdapter(adapter);
        setFocusableInTouchMode(true);
        nodes = new ArrayList<>();
    }

    /**
     * Interface definition for a callback for value change events in the form.
     */
    public interface FormValueChangedListener {
        /**
         * Called whenever a value changes in the form.
         *
         * @param sectionId Section where a value was changed
         * @param fieldId   Field where a value was changed
         * @param value     New value
         */
        void onFormValueChanged(int sectionId, int fieldId, Object value);
    }

    /**
     * Interface definition for a callback to be invoked whenever the form is clicked.
     */
    public interface FormClickListener {
        /**
         * Called whenever the form is clicked.
         *
         * @param sectionId Section where the click occurred
         * @param fieldId   Field in a Section where the click occurred
         */
        void onFormClick(int sectionId, int fieldId);
    }

    /**
     * Interface definition for a callback to be invoked whenever the focus changes in the form.
     */
    public interface FormFocusChangeListener {
        /**
         * Called whenever the form focus changes.
         *
         * @param sectionId Section where the focus changed
         * @param fieldId   Field in a Section where the focus changed
         * @param hasFocus  Whether the element has focus now
         */
        void onFormFocusChange(int sectionId, int fieldId, boolean hasFocus);
    }

    /**
     * Data Source definition required for rendering the form.
     */
    public interface DataSource {
        /**
         * Returns the number of sections in the form.
         *
         * @return number of sections
         */
        int getSectionCount();

        /**
         * Returns the number of fields in a section.
         *
         * @param sectionId Section index
         * @return number of fields in the section
         */
        int getFieldCount(int sectionId);

        /**
         * Returns the model describing the field.
         * <p>
         * Look at {@link FormModel} to see available models.
         *
         * @param sectionId Section index
         * @param fieldId   Field index
         * @return Model representing the type of field
         */
        @NonNull
        FormModel getModel(int sectionId, int fieldId);

        /**
         * Returns an optional header for a section. If present it will be displayed before all the fields in the section.
         * <p>
         * Go to {@link FormHeader} for more information.
         *
         * @param sectionId Section index
         * @return Optional header
         */
        @Nullable
        FormHeader getSectionHeader(int sectionId);

        /**
         * Returns an optional message for a field in the form. If present the message will appear under the field.
         * <p>
         * Go to {@link FormMessage} for more information.
         *
         * @param sectionId Section index
         * @param fieldId   Field index
         * @return Optional message for a field
         */
        @Nullable
        FormMessage getMessage(int sectionId, int fieldId);

        /**
         * Returns the value for a specific form field. The value must match to the form model type for correct behaviour.
         * If incorrect object types are given, class cast or ambiguous form behaviour might occur.
         * <p>
         * See {@link FormModel} and derived classes for expected values for each type.
         *
         * @param sectionId Section index
         * @param fieldId   Field index
         * @return Current value for the field
         */
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
