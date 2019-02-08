package com.guestlogix.traveleruikit.forms;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.adapters.FormAdapter;
import com.guestlogix.traveleruikit.forms.cells.FormCell;
import com.guestlogix.traveleruikit.forms.models.BaseElement;
import com.guestlogix.traveleruikit.forms.utilities.FormBuilder;

import java.util.List;

/**
 * A form layout used to display a list of sections each containing a set of questions.
 */
public class Form extends FrameLayout {

    private RecyclerView cellsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FormAdapter adapter;
    private FormBuilder builder;

    /**
     * Unidirectional map which maps the Absolute index into sectionId/inputId pair.
     */
    private List<Pair<Integer, Integer>> indexMap;

    /**
     * Callback interface used to notify any subscriber whenever a click was performed on an TextCell in the form.
     */
    protected OnFormClickListener onFormClickListener;

    /**
     * Callback interface used to notify any subscriber whenever a long click was performed on an TextCell in the form.
     */
    protected OnFormLongClickListener onFormLongClickListener;

    /**
     * Callback Interface used to notify any subscriber whenever a value was changed in the form.
     */
    protected OnFormValueChangedListener onFormValueChangedListener;

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

    public void updateView(BaseElement e) {
        int pos = e.getIndex();
        FormCell cell = (FormCell) cellsRecyclerView.findViewHolderForLayoutPosition(pos);
        e.updateCell(cell);
    }

    public void setError(int position, String error) {
        FormCell cell = (FormCell) cellsRecyclerView.findViewHolderForLayoutPosition(position);
    }

    /**
     * Uses the {@link FormBuilder} to initialize the form layout.
     *
     * @param builder FormBuilder
     */
    public void initialize(FormBuilder builder) {
        initialize(builder, new LinearLayoutManager(getContext()));
    }

    public void initialize(FormBuilder builder, RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        this.builder = builder;
        adapter = new FormAdapter(formMapper);
        adapter.setContextRequestListener(this.contextRequestListener);

        builder.setOnElementValueChangedListener(this::onElementValueChange);

        cellsRecyclerView.setLayoutManager(layoutManager);
        cellsRecyclerView.setAdapter(adapter);
    }

    public void reload() {

    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_form, this, true);
            cellsRecyclerView = view.findViewById(R.id.sections);
        }
    }

    private void onElementValueChange(BaseElement element) {
        if (null != this.onFormValueChangedListener) {
            this.onFormValueChangedListener.onFormValueChanged(element.getIndex(), "TestValue");
        }
    }

    FormAdapter.FormMapper formMapper = new FormAdapter.FormMapper() {
        @Override
        public int getTotalCount() {
            return builder.getSize();
        }

        @Override
        public int getViewType(int position) {
            return builder.getElementType(position);
        }

        @Override
        public FormCell createViewHolder(ViewGroup parent, int type) {
            return builder.createFormCell(parent, type);
        }

        @Override
        public void bindView(FormCell cell, int position) {
            builder.bindFormCell(cell, position);
        }
    };

    /**
     * Subscribes to click events.
     *
     * @param onFormClickListener callback method for click events.
     */
    public void setOnFormClickListener(OnFormClickListener onFormClickListener) {
        this.onFormClickListener = onFormClickListener;
    }

    /**
     * Subscribes to long click events.
     *
     * @param onFormLongClickListener callback method for long click events.
     */
    public void setOnFormLongClickListener(OnFormLongClickListener onFormLongClickListener) {
        this.onFormLongClickListener = onFormLongClickListener;
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
     * This listener is used to indicate that an input value was clicked in the {@link Form}.
     */
    public interface OnFormClickListener {
        /**
         * Is invoked whenever the user clicked on an TextCell in the form.
         *
         * @param sectionId HeaderCell index where the click was done.
         * @param inputId   TextCell index within the section where the click was done.
         */
        void onFormClick(int sectionId, int inputId);
    }

    /**
     * This listener is used to indicate that an input value was long clicked.
     */
    public interface OnFormLongClickListener {
        /**
         * Is invoked whenever the TextCell section was long clicked.
         *
         * @param sectionId HeaderCell index where the long click was performed.
         * @param inputId   TextCell index within the section where the long click was performed.
         * @return Whether the event was consumed.
         */
        boolean onFormLongClick(int sectionId, int inputId);
    }

    /**
     * This listener is used to indicate that an input value has been changed.
     */
    public interface OnFormValueChangedListener {
        /**
         * Is invoked whenever an TextCell had his value changed.
         *
         * @param position Relative position of the element in the form where the value was changed.
         * @param newValue Current value of the input.
         */
        void onFormValueChanged(int position, CharSequence newValue);
    }

    FormAdapter.OnFormContextRequestListener contextRequestListener = this::getContext;
}
