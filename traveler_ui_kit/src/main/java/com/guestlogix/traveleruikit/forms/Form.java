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
import com.guestlogix.traveleruikit.forms.utilities.FormBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * A form layout used to render and display a flat form. The implementing class has to provide a FormBuilder object
 * which hosts the underlying structure of the form.
 * <p>
 * Since the form takes a flat object structure, the implementing activity/fragment must have a flattening strategy.
 * <p>
 * All form events are not guaranteed to be invoked. It is up-to individual {@link BaseCell}s to implement event listeners
 * and dispatch the appropriate events.
 */
@SuppressLint("UseSparseArrays")
public class Form extends FrameLayout {

    private RecyclerView cellsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FormRecyclerViewAdapter rvAdapter;
    private DataSource dataSource;
    private Builder builder;
    private Map<Integer, Pair<Integer, Integer>> posToPair;
    private Map<Integer, Integer> posToType;
    private Map<Pair<Integer, Integer>, Boolean> hasMessage;
    private int count;

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
    private FormRecyclerViewAdapter.FormMapper formMapper = new FormRecyclerViewAdapter.FormMapper() {
        @Override
        public int getTotalCount() {
            return count;
        }

        @Override
        public int getViewType(int position) {
            if (!posToType.containsKey(position)) {
                return 0;
            }

            Integer type = posToType.get(position);
            if (type == null) {
                type = 0;
            }
            return type;
        }

        @Override
        public BaseCell createViewHolder(ViewGroup parent, int type) {
            return builder.inflateBaseCell(parent, type);
        }

        @Override
        public void bindView(BaseCell cell, int position) {
            Integer type = posToType.get(position);
            Pair<Integer, Integer> pos = posToPair.get(position);

            if (type == null || pos == null) {
                throw new RuntimeException("Out of bounds position has been found by the form.");
            }

            switch (FormType.valueOf(type)) {
                case HEADER:
                    HeaderCell headerCell = (HeaderCell) cell;
                    headerCell.setTitle(dataSource.getTitle(pos.first));
                    headerCell.setSubtitle(dataSource.getDisclaimer(pos.first));
                    break;
                case TEXT:
                    TextCell textCell = (TextCell) cell;
                    TextDescriptor textDescriptor = (TextDescriptor) dataSource.getInputDescriptor(pos.first, pos.second, type);
                    textCell.setValue((String) dataSource.getValue(pos.first, pos.second));
                    textCell.setHint(textDescriptor.hint);
                    break;
                case QUANTITY:
                    QuantityCell quantityCell = (QuantityCell) cell;
                    QuantityDescriptor quantityDescriptor = (QuantityDescriptor) dataSource.getInputDescriptor(pos.first, pos.second, type);
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
                            Integer value = (Integer) dataSource.getValue(pos.first, pos.second);
                            if (value == null) {
                                value = quantityDescriptor.minQuantity;
                            }
                            return value;
                        }
                    });
                    break;
                case BUTTON:
                    ButtonCell buttonCell = (ButtonCell) cell;
                    ButtonDescriptor buttonDescriptor = (ButtonDescriptor) dataSource.getInputDescriptor(pos.first, pos.second, type);
                    buttonCell.setText(buttonDescriptor.text);
                    break;
                case SPINNER:
                    SpinnerCell spinnerCell = (SpinnerCell) cell;
                    SpinnerDescriptor spinnerDescriptor = (SpinnerDescriptor) dataSource.getInputDescriptor(pos.first, pos.second, type);
                    spinnerCell.setHint(spinnerDescriptor.title);
                    spinnerCell.setOptions(spinnerDescriptor.options, spinnerDescriptor.value);
                    break;
                case MESSAGE:
                    MessageCell messageCell = (MessageCell) cell;
                    Pair<String, FormMessage> msg = dataSource.getMessage(pos.first, pos.second);

                    if (msg != null) {
                        messageCell.setMessage(msg.first, msg.second);
                    } else {
                        messageCell.reload();
                    }
                    break;
                default:
                    builder.bindBaseCell(cell, type);

            }
        }
    };

    /**
     * Reloads all elements in the form.
     */
    public void reload() {
        count = 0;
        posToPair = new HashMap<>();
        posToType = new HashMap<>();

        for (int i = 0; i < dataSource.getSectionCount(); i++) {
            String title = dataSource.getTitle(i);
            String disclaimer = dataSource.getDisclaimer(i);

            if (title != null || disclaimer != null) {
                // Register Header.
                posToPair.put(count, new Pair<>(i, -1));
                posToType.put(count++, FormType.HEADER.value);
            }

            for (int j = 0; j < dataSource.getFieldCount(i); j++) {
                // Add input field.
                posToPair.put(count, new Pair<>(i, j));
                posToType.put(count++, dataSource.getType(i, j));

                // Check if it has a message.
                if (dataSource.getMessage(i, j) != null) {
                    posToPair.put(count, new Pair<>(i, j));
                    posToType.put(count++, FormType.MESSAGE.value);
                }
            }
        }

        if (rvAdapter != null) {
            rvAdapter.notifyDataSetChanged();
        }
    }

    public void setDataSource(@NonNull DataSource dataSource) {
        setDataSource(dataSource, new LinearLayoutManager(getContext()));
    }

    public void setDataSource(@NonNull DataSource dataSource, RecyclerView.LayoutManager layoutManager) {
        this.dataSource = dataSource;
        this.layoutManager = layoutManager;
        reload();

        if (null == builder) {
            builder = new BaseBuilder(getContext());
        }

        rvAdapter = new FormRecyclerViewAdapter(formMapper);
        rvAdapter.setContextRequestListener(contextRequestListener);
        rvAdapter.setCellEventsListener(cellEventsListener);

        cellsRecyclerView.setLayoutManager(this.layoutManager);
        cellsRecyclerView.setAdapter(rvAdapter);
        cellsRecyclerView.setItemAnimator(animator);
    }

    public Builder getBuilder() {
        return builder;
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_form, this, true);
            cellsRecyclerView = view.findViewById(R.id.sections);
        }
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;

        if (dataSource != null) {
            rvAdapter.notifyDataSetChanged();
        }
    }

    private FormRecyclerViewAdapter.CellEventsListener cellEventsListener = new FormRecyclerViewAdapter.CellEventsListener() {
        @Override
        public void onValueChanged(int pos, Object value) {
            if (onFormValueChangedListener != null) {
                Pair<Integer, Integer> p = posToPair.get(pos);

                if (p != null) {
                    onFormValueChangedListener.onFormValueChanged(p.first, p.second, value);
                }
            }
        }

        @Override
        public void onFocusChanged(int pos, boolean hasFocus) {
            if (onFormFocusChangedListener != null) {
                Pair<Integer, Integer> p = posToPair.get(pos);

                if (p != null) {
                    onFormFocusChangedListener.onFormFocusChanged(p.first, p.second, hasFocus);
                }
            }
        }

        @Override
        public void onClick(int pos) {
            if (onFormClickListener != null) {
                Pair<Integer, Integer> p = posToPair.get(pos);

                if (p != null) {
                    onFormClickListener.onFormClick(p.first, p.second);
                }
            }
        }
    };

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

    public interface DataSource {
        int getSectionCount();

        int getFieldCount(int sectionId);

        int getType(int sectionId, int fieldId);

        @NonNull
        InputDescriptor getInputDescriptor(int sectionId, int fieldId, int type);

        @Nullable
        String getTitle(int sectionId);

        @Nullable
        String getDisclaimer(int sectionId);

        @Nullable
        Pair<String, FormMessage> getMessage(int sectionId, int fieldId);

        @Nullable
        Object getValue(int sectionId, int fieldId);
    }

    /**
     * Class which handles all information regarding inflating the layouts and binding the views.
     * <p>
     * See {@link BaseBuilder} for default implementation of form components.
     */
    public static abstract class Builder {
        Context context;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public abstract BaseCell inflateBaseCell(ViewGroup parent, int type);

        public abstract void bindBaseCell(BaseCell baseCell, int type);
    }

    /**
     * <p>
     * Default implementation for the {@link Builder} class.
     * Contains code to implement all cells present in the {@link FormType} enum.
     * </p>
     * <p>
     * You can also include your own layout by calling the {@link #registerCustomCell(FormBuilder.CustomCellAdapter)} method.</p>
     */
    public static class BaseBuilder extends Builder {
        private static int FORM_TYPE_COUNT;

        static {
            FORM_TYPE_COUNT = FormType.getTypeCount();
        }

        private Map<Integer, FormBuilder.CustomCellAdapter> customComponentMap = new HashMap<>();
        private int customCellCount;

        BaseBuilder(@NonNull Context context) {
            super(context);
            customCellCount = 0;
        }

        /**
         * Registers a custom form cell element to be used in the form. The integer value returned by this method will used
         * to instantiate views and models as they are needed.
         *
         * @param strategy Methods to instantiate custom elements.
         * @return Type of the current custom cell.
         */
        public int registerCustomCell(@NonNull FormBuilder.CustomCellAdapter strategy) {
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
        public BaseCell inflateBaseCell(ViewGroup parent, int elementType) {
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
                    return createCustomCell(parent, elementType);
            }
        }

        @Override
        public void bindBaseCell(BaseCell cell, int type) {
            if (type > FORM_TYPE_COUNT) {
                bindCustomCell(cell, null, type);
            } else {

            }
        }

        void bindCustomCell(BaseCell cell, InputDescriptor descriptor, int type) {
            if (customComponentMap.containsKey(type)) {
                FormBuilder.CustomCellAdapter adapter = customComponentMap.get(type);

                if (null != adapter) {
                    adapter.bindCell(cell, descriptor, type);
                    return;
                }
            }
            throw new RuntimeException("Adapter not found. Register adapter with registerCustomCell()");
        }

        private BaseCell createCustomCell(ViewGroup parent, int type) {
            FormBuilder.CustomCellAdapter adapter = customComponentMap.get(type);

            if (null != adapter) {
                return adapter.inflateCustomCell(context, parent);
            }

            throw new RuntimeException("Invalid cell type. Please make sure any custom types have been registered with the FormBuilder");
        }
    }

    protected static class FormLayout {
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
