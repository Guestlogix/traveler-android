package com.guestlogix.traveleruikit.forms;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.adapters.FormRecyclerViewAdapter;
import com.guestlogix.traveleruikit.forms.cells.*;
import com.guestlogix.traveleruikit.forms.descriptors.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Form layout used to translate a double indexed section and field data type into a single index recycler view.
 *
 * Use {@link FormBuilder} to create custom views or use {@link FormType} base fields.
 * This form must be given a {@link DataSource} to start rendering.
 */
@SuppressLint("UseSparseArrays")
public class Form extends FrameLayout {

    private RecyclerView cellsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FormRecyclerViewAdapter rvAdapter;
    private DataSource dataSource;
    private FormBuilder formBuilder;
    private ArrayList<LinkedFormNode> nodes;
    private Map<Pair<Integer, Integer>, Integer> sectionToPos;

    /**
     * Callback interface used to notify any subscriber whenever a click was performed on an TextCell in the form.
     */
    protected OnFormClickListener onFormClickListener;

    /**
     * Callback Interface used to notify any subscriber whenever a value was changed in the form.
     */
    protected OnFormValueChangedListener onFormValueChangedListener;

    /**
     * Callback interface used to notify any subscriber whenever a focus was changed in an element of the form.
     */
    protected OnFormFocusChangedListener onFormFocusChangedListener;

    public Form(@NonNull Context context) {
        super(context);
        initView(context, null, 0, 0);
    }

    public Form(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, 0);
    }

    public Form(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Form(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private FormRecyclerViewAdapter.CellEventsListener cellEventsListener = new FormRecyclerViewAdapter.CellEventsListener() {
        @Override
        public void onValueChanged(int pos, Object value) {
            if (onFormValueChangedListener != null) {
                LinkedFormNode node = nodes.get(pos);
                onFormValueChangedListener.onFormValueChanged(node.sectionId, node.fieldId, value);
            }
        }

        @Override
        public void onFocusChanged(int pos, boolean hasFocus) {
            if (onFormFocusChangedListener != null) {
                LinkedFormNode node = nodes.get(pos);
                onFormFocusChangedListener.onFormFocusChanged(node.sectionId, node.fieldId, hasFocus);
            }
        }

        @Override
        public void onClick(int pos) {
            if (onFormClickListener != null) {
                LinkedFormNode node = nodes.get(pos);
                onFormClickListener.onFormClick(node.sectionId, node.fieldId);
            }
        }
    };
    private FormRecyclerViewAdapter.FormMapper formMapper = new FormRecyclerViewAdapter.FormMapper() {
        @Override
        public int getTotalCount() {
            return nodes.size();
        }

        @Override
        public int getViewType(int position) {
            return nodes.get(position).type;
        }

        @Override
        public BaseCell createViewHolder(ViewGroup parent, int type) {
            return formBuilder.inflateBaseCell(parent, type);
        }

        @Override
        public void bindView(BaseCell cell, int position) {
            LinkedFormNode node = nodes.get(position);

            // Checks if a message needs to be displayed or removed.
            if (node.type != FormType.MESSAGE.value && node.type != FormType.HEADER.value) {
                Pair<String, FormMessage> msg = dataSource.getMessage(node.sectionId, node.fieldId);

                if (msg != null && !node.hasChild) {
                    node.hasChild = true;
                    LinkedFormNode msgNode = new LinkedFormNode(node.sectionId, node.fieldId, FormType.MESSAGE.value);
                    nodes.add(position + 1, msgNode);
                    rvAdapter.notifyItemInserted(position + 1);
                } else if (msg == null && node.hasChild) {
                    node.hasChild = false;
                    nodes.remove(position + 1);
                    rvAdapter.notifyItemRemoved(position + 1);
                }
            }

            formBuilder.bindBaseCell(cell, node.sectionId, node.fieldId, node.type);
        }
    };

    /**
     * Hard reload of all elements in the form.
     */
    public void reload() {
        nodes = new ArrayList<>();
        sectionToPos = new HashMap<>();

        for (int i = 0; i < dataSource.getSectionCount(); i++) {
            String title = dataSource.getTitle(i);
            String disclaimer = dataSource.getDisclaimer(i);

            if (title != null || disclaimer != null) {
                // Section has a header.
                LinkedFormNode node = new LinkedFormNode(i, -1, FormType.HEADER.value);
                sectionToPos.put(new Pair<>(i, -1), nodes.size());
                nodes.add(node);
            }

            // Field in this section
            for (int j = 0; j < dataSource.getFieldCount(i); j++) {
                LinkedFormNode node = new LinkedFormNode(i, j, dataSource.getType(i, j));
                sectionToPos.put(new Pair<>(i, -1), nodes.size());
                nodes.add(node);
            }
        }

        if (rvAdapter != null) {
            rvAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Reloads a specific cell in the form.
     *
     * @param sectionId Section where an item was reloaded.
     * @param fieldId   Optional field which was reloaded.
     */
    public void reload(@NonNull Integer sectionId, @Nullable Integer fieldId) {
        if (fieldId == null) {
            fieldId = -1;
        }

        Integer pos = sectionToPos.get(new Pair<>(sectionId, fieldId));

        if (pos != null && rvAdapter != null) {
            rvAdapter.notifyItemChanged(pos);
        }
    }

    /**
     * Scrolls to a specific field int the form. If fieldId is left null, will scroll to the header
     *
     * @param sectionId section index.
     * @param fieldId   field index.
     */
    public void smoothScrollToPosition(@NonNull Integer sectionId, @Nullable Integer fieldId) {
        if (fieldId == null) {
            fieldId = -1;
        }

        Integer pos = sectionToPos.get(new Pair<>(sectionId, fieldId));

        if (pos != null && rvAdapter != null) {
            cellsRecyclerView.smoothScrollToPosition(pos);
        }
    }

    /**
     * Sets the data source and starts building the form.
     * Equivalent to calling {@link #setDataSource(DataSource, RecyclerView.LayoutManager)} with a linear layout.
     *
     * @param dataSource Callback for information required by the form.
     */
    public void setDataSource(@NonNull DataSource dataSource) {
        setDataSource(dataSource, new LinearLayoutManager(getContext()));
    }

    /**
     * Sets the data source and begins rendering the form.
     *
     * @param dataSource Callback for information required by the form.
     * @param layoutManager Layout Manager
     */
    public void setDataSource(@NonNull DataSource dataSource, RecyclerView.LayoutManager layoutManager) {
        this.dataSource = dataSource;
        this.layoutManager = layoutManager;
        reload();

        if (null == formBuilder) {
            formBuilder = new BaseFormBuilder(getContext(), new BaseFormBuilder.BuilderDataSource() {
                @Override
                public Pair<String, FormMessage> getMessage(int sectionId, int fieldId) {
                    return dataSource.getMessage(sectionId, fieldId);
                }

                @Override
                public InputDescriptor getInputDescriptor(int sectionId, int fieldId, int type) {
                    return dataSource.getInputDescriptor(sectionId, fieldId, type);
                }

                @Override
                public String getTitle(int sectionId) {
                    return dataSource.getTitle(sectionId);
                }

                @Override
                public String getSubtitle(int sectionId) {
                    return dataSource.getDisclaimer(sectionId);
                }

                @Override
                public Object getValue(int sectionId, int fieldId) {
                    return dataSource.getValue(sectionId, fieldId);
                }
            });
        }

        rvAdapter = new FormRecyclerViewAdapter(formMapper);
        rvAdapter.setContextRequestListener(contextRequestListener);
        rvAdapter.setCellEventsListener(cellEventsListener);

        cellsRecyclerView.setLayoutManager(this.layoutManager);
        cellsRecyclerView.setAdapter(rvAdapter);
        cellsRecyclerView.setItemAnimator(animator);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_form, this, true);
            cellsRecyclerView = view.findViewById(R.id.sections);
        }
    }

    /**
     * Gets current {@link FormBuilder} class in the form.
     * <p>By default it contains a {@link BaseFormBuilder} where you can register custom cell types.</p>
     *
     * @return Current formBuilder for the form.
     */
    public FormBuilder getFormBuilder() {
        return formBuilder;
    }

    /**
     * Subscribes to value changed events.
     *
     * @param onFormValueChangedListener callback method for text change events.
     */
    public void setOnFormValueChangedListener(OnFormValueChangedListener onFormValueChangedListener) {
        this.onFormValueChangedListener = onFormValueChangedListener;
    }

    /**
     * Subscribes to cell focus change events.
     *
     * @param onFormFocusChangedListener callback method for focus change events.
     */
    public void setOnFormFocusChangedListener(OnFormFocusChangedListener onFormFocusChangedListener) {
        this.onFormFocusChangedListener = onFormFocusChangedListener;
    }

    /**
     * Subscribes to cell click events.
     *
     * @param onFormClickListener callback method for click events.
     */
    public void setOnFormClickListener(OnFormClickListener onFormClickListener) {
        this.onFormClickListener = onFormClickListener;
    }

    /**
     * This listener is used to indicate that a cell was clicked.
     */
    public interface OnFormClickListener {
        /**
         * Is invoked whenever a click was performed in the form.
         *
         * @param sectionId Section where the field happened.
         * @param fieldId   Field within the Section where the click happened.
         */
        void onFormClick(int sectionId, int fieldId);
    }

    /**
     * This listener is used to indicate that an input value has been changed.
     */
    public interface OnFormValueChangedListener {
        /**
         * Is invoked whenever a value is changed in the form.
         *
         * @param sectionId Section where the value was changed.
         * @param fieldId   Field which was changed in the Section.
         * @param value     new value for the field.
         */
        void onFormValueChanged(int sectionId, int fieldId, Object value);
    }

    /**
     * This listener is used to indicate that the focus has changed for a given input field.
     */
    public interface OnFormFocusChangedListener {
        /**
         * Is invoked whenever a focus change happens for a field in the form.
         *
         * @param sectionId Section where the event occurred.
         * @param fieldId   Fields in the Section where the event occurred.
         * @param hasFocus  Whether the field has focus now.
         */
        void onFormFocusChanged(int sectionId, int fieldId, boolean hasFocus);
    }

    FormRecyclerViewAdapter.OnFormContextRequestListener contextRequestListener = this::getContext;

    /**
     * Currently out of the box, supported form cell types.
     */
    public enum FormType {
        HEADER(0), TEXT(1), BUTTON(2), QUANTITY(3), SPINNER(4), MESSAGE(5);

        private static Map<Integer, FormType> map = new HashMap<>();

        static {
            for (FormType type : FormType.values()) {
                map.put(type.value, type);
            }
        }

        private int value;

        FormType(int value) {
            this.value = value;
        }

        public static FormType valueOf(int formType) {
            return map.get(formType);
        }

        public static int getTypeCount() {
            return map.size();
        }

        public int getValue() {
            return value;
        }
    }

    DefaultItemAnimator animator = new DefaultItemAnimator() {
        @Override
        public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
            return true;
        }
    };

    public enum FormMessage {
        INFO, ALERT
    }

    public void setFormBuilder(FormBuilder formBuilder) {
        this.formBuilder = formBuilder;

        if (dataSource != null) {
            rvAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Callback interface for fetching information for the Form.
     */
    public interface DataSource {
        /**
         * Amount of sections in the form.
         *
         * @return section count.
         */
        int getSectionCount();

        /**
         * Amount of fields in a specific section.
         * @param sectionId Section index.
         * @return amount of fields for the section index.
         */
        int getFieldCount(int sectionId);

        /**
         * Type of a specific field as dictated by a {@link FormBuilder} class.
         *
         * @param sectionId section where the field is.
         * @param fieldId field in the section.
         * @return type of the specific field.
         */
        int getType(int sectionId, int fieldId);

        /**
         * Input descriptor for a specific cell.
         *
         * @param sectionId section index where the field is.
         * @param fieldId field index where the field is.
         * @param type type of the field.
         * @return InputDescriptor for a given type.
         */
        @NonNull
        InputDescriptor getInputDescriptor(int sectionId, int fieldId, int type);

        /**
         * Title of a section.
         * @param sectionId Section index.
         * @return Optional title for the section.
         */
        @Nullable
        String getTitle(int sectionId);

        /**
         * Disclaimer of a section.
         * @param sectionId Section index.
         * @return Optional disclaimer for the section.
         */
        @Nullable
        String getDisclaimer(int sectionId);

        /**
         * Message for a specific field. See {@link FormMessage} for supported message types.
         * @param sectionId section index.
         * @param fieldId field index.
         * @return pair of message and type.
         */
        @Nullable
        Pair<String, FormMessage> getMessage(int sectionId, int fieldId);

        /**
         * Get the value for a field. If null will treat as if there was never a value set in the field.
         * @param sectionId section index.
         * @param fieldId field index.
         * @return value for the field. Assumes it can be cast to correct type when binding.
         */
        @Nullable
        Object getValue(int sectionId, int fieldId);
    }

    /**
     * Base FormBuilder class.
     * A FormBuilder handles inflation and bindings of custom view types which are not handled by the form. It is
     * responsible for assigning correct ids to every element.
     *
     * <p>
     * See {@link BaseFormBuilder} for default implementation of form components.
     * </p>
     */
    public static abstract class FormBuilder {
        Context context;

        FormBuilder(@NonNull Context context) {
            this.context = context;
        }

        abstract BaseCell inflateBaseCell(ViewGroup parent, int type);

        abstract void bindBaseCell(BaseCell baseCell, int sectionId, int fieldId, int type);
    }

    /**
     * <p>
     * Default implementation for the {@link FormBuilder} class.
     * Contains code to implement all cells present in the {@link FormType} enum.
     * </p>
     * <p>
     * You can also include your own layout by calling the {@link #registerCustomCell(CustomCellAdapter)} method.</p>
     */
    public static class BaseFormBuilder extends FormBuilder {
        private static int FORM_TYPE_COUNT;

        static {
            FORM_TYPE_COUNT = FormType.getTypeCount();
        }

        private Map<Integer, CustomCellAdapter> customComponentMap = new HashMap<>();
        private int customCellCount;
        private BuilderDataSource dataSource;

        BaseFormBuilder(@NonNull Context context, BuilderDataSource dataSource) {
            super(context);
            customCellCount = 0;
            this.dataSource = dataSource;
        }

        /**
         * Registers a custom form cell element to be used in the form. The integer value returned by this method will used
         * to instantiate views and models as they are needed.
         *
         * @param strategy Methods to instantiate custom elements.
         * @return Type of the current custom cell.
         */
        public int registerCustomCell(@NonNull CustomCellAdapter strategy) {
            int type = FORM_TYPE_COUNT + customCellCount;
            customComponentMap.put(type, strategy);
            customCellCount++;
            return type;
        }

        /**
         * Inflates the correct cell class based on the given type.
         *
         * @param parent      Parent group to attach to.
         * @param elementType Type of cell to update.
         * @return Cell of the given type.
         */
        @Override
        BaseCell inflateBaseCell(ViewGroup parent, int elementType) {
            View view;
            switch (FormType.valueOf(elementType)) {
                case HEADER:
                    view = LayoutInflater.from(context).inflate(FormLayout.HEADER_LAYOUT, parent, false);
                    return new HeaderCell(view);
                case BUTTON:
                    view = LayoutInflater.from(context).inflate(FormLayout.BUTTON_LAYOUT, parent, false);
                    return new ButtonCell(view);
                case TEXT:
                    view = LayoutInflater.from(context).inflate(FormLayout.TEXT_LAYOUT, parent, false);
                    return new TextCell(view);
                case QUANTITY:
                    view = LayoutInflater.from(context).inflate(FormLayout.QUANTITY_LAYOUT, parent, false);
                    return new QuantityCell(view);
                case SPINNER:
                    view = LayoutInflater.from(context).inflate(FormLayout.SPINNER_LAYOUT, parent, false);
                    return new SpinnerCell(view);
                case MESSAGE:
                    view = LayoutInflater.from(context).inflate(FormLayout.MESSAGE_LAYOUT, parent, false);
                    return new MessageCell(view);
                default:
                    CustomCellAdapter adapter = customComponentMap.get(elementType);

                    if (null != adapter) {
                        return adapter.inflateCustomCell(context, parent);
                    }

                    throw new RuntimeException("Invalid cell type. Please make sure any custom types have been registered with the FormBuilder");
            }
        }

        /**
         * Binds the default cells with values or tries to find an adapter to bind other cells.
         *
         * @param cell Cell to be populated.
         * @param sectionId Section where this cell is.
         * @param fieldId Field where this cell is.
         * @param type Type of cell as defined by the adapter.
         */
        @Override
        void bindBaseCell(BaseCell cell, int sectionId, int fieldId, int type) {
            switch (FormType.valueOf(type)) {
                case HEADER:
                    HeaderCell headerCell = (HeaderCell) cell;
                    headerCell.setTitle(dataSource.getTitle(sectionId));
                    headerCell.setSubtitle(dataSource.getSubtitle(sectionId));
                    break;
                case TEXT:
                    TextCell textCell = (TextCell) cell;
                    TextDescriptor textDescriptor = (TextDescriptor) dataSource.getInputDescriptor(sectionId, fieldId, type);
                    textCell.setValue((String) dataSource.getValue(sectionId, fieldId));
                    textCell.setHint(textDescriptor.hint);
                    break;
                case QUANTITY:
                    QuantityCell quantityCell = (QuantityCell) cell;
                    QuantityDescriptor quantityDescriptor = (QuantityDescriptor) dataSource.getInputDescriptor(sectionId, fieldId, type);
                    quantityCell.setQuantity(quantityDescriptor.value == null ? "0" : quantityDescriptor.value.toString());
                    quantityCell.setTitle(quantityDescriptor.title);
                    quantityCell.setSubtitle(quantityDescriptor.subtitle);
                    quantityCell.setAdapter(new QuantityCell.QuantityCellAdapter() {
                        @Override
                        public String getTitle() {
                            return quantityDescriptor.title;
                        }

                        @Nullable
                        @Override
                        public Integer getMaxQuantity() {
                            return quantityDescriptor.maxQuantity;
                        }

                        @NonNull
                        @Override
                        public Integer getMinQuantity() {
                            return quantityDescriptor.minQuantity;
                        }

                        @NonNull
                        @Override
                        public Integer getValue() {
                            Integer value = (Integer) dataSource.getValue(sectionId, fieldId);
                            if (value == null) {
                                value = quantityDescriptor.minQuantity;
                            }
                            return value;
                        }
                    });
                    break;
                case BUTTON:
                    ButtonCell buttonCell = (ButtonCell) cell;
                    ButtonDescriptor buttonDescriptor = (ButtonDescriptor) dataSource.getInputDescriptor(sectionId, fieldId, type);
                    buttonCell.setText(buttonDescriptor.text);
                    break;
                case SPINNER:
                    SpinnerCell spinnerCell = (SpinnerCell) cell;
                    SpinnerDescriptor spinnerDescriptor = (SpinnerDescriptor) dataSource.getInputDescriptor(sectionId, fieldId, type);
                    spinnerCell.setHint(spinnerDescriptor.title);
                    spinnerCell.setOptions(spinnerDescriptor.options, spinnerDescriptor.value);
                    break;
                case MESSAGE:
                    MessageCell messageCell = (MessageCell) cell;
                    Pair<String, FormMessage> msg = dataSource.getMessage(sectionId, fieldId);

                    if (msg != null) {
                        messageCell.setMessage(msg.first, msg.second);
                    } else {
                        messageCell.reload();
                    }
                    break;
                default:
                    if (customComponentMap.containsKey(type)) {
                        CustomCellAdapter adapter = customComponentMap.get(type);

                        if (null != adapter) {
                            adapter.bindCell(cell, sectionId, fieldId, type);
                            return;
                        }
                    }
                    throw new RuntimeException("Adapter not found. Register adapter with registerCustomCell()");
            }
        }

        public interface CustomCellAdapter {
            BaseCell inflateCustomCell(Context context, ViewGroup parent);

            void bindCell(BaseCell cell, int sectionId, int fieldId, int type);
        }

        interface BuilderDataSource {
            Pair<String, Form.FormMessage> getMessage(int sectionId, int fieldId);

            InputDescriptor getInputDescriptor(int sectionId, int fieldId, int type);

            String getTitle(int sectionId);

            String getSubtitle(int sectionId);

            Object getValue(int sectionId, int fieldId);
        }

        static class FormLayout {
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
        }
    }

    // Node which holds all the information necessary to display nodes.
    private class LinkedFormNode {
        int sectionId;
        int fieldId;
        int type;
        boolean hasChild;

        LinkedFormNode(int sectionId, int fieldId, int type) {
            this.sectionId = sectionId;
            this.fieldId = fieldId;
            this.type = type;
            this.hasChild = false;
        }
    }
}
