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
import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.models.BaseElement;
import com.guestlogix.traveleruikit.forms.utilities.FormBuilder;

import java.util.List;

/**
 * A form layout used to render and display a flat form. The implementing class has to provide a FormBuilder object
 * which hosts the underlying structure of the form.
 *
 * Since the form takes a flat object structure, the implementing activity/fragment must have a flattening strategy.
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
        BaseCell cell = (BaseCell) cellsRecyclerView.findViewHolderForLayoutPosition(pos);
        scrollToPosition(pos);
        e.updateCell(cell);
    }

    public void setError(int position, String error) {
        BaseElement element = builder.getElement(position);
        BaseCell cell = (BaseCell) cellsRecyclerView.findViewHolderForLayoutPosition(position);
        element.setState(BaseElement.State.ERROR);
        element.setErrorMessage(error);
        scrollToPosition(position);
        element.updateCell(cell);
    }

    /**
     *
     * @param position
     * @param info
     */
    public void setInfo(int position, String info) {
        BaseCell cell = (BaseCell) cellsRecyclerView.findViewHolderForLayoutPosition(position);
    }

    public void scrollToPosition(int position) {
        layoutManager.scrollToPosition(position);
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
        builder.setOnElementClickListener(this::onElementClick);

        cellsRecyclerView.setLayoutManager(layoutManager);
        cellsRecyclerView.setAdapter(adapter);
    }

    /**
     * Reloads all elements in the form.
     */
    public void reload() {
        builder.reloadAll();
    }

    /**
     * Reloads a specific element in the form.
     *
     * @param position index of the element to reload.
     */
    public void reload(int position) {
        BaseElement element = builder.getElement(position);
        element.reload();
        updateView(element);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_form, this, true);
            cellsRecyclerView = view.findViewById(R.id.sections);
        }
    }

    private void onElementClick(BaseElement element) {
        if (null != this.onFormClickListener) {
            onFormClickListener.onFormClick(element.getIndex());
        }
    }

    private void onElementValueChange(BaseElement element) {
        if (null != this.onFormValueChangedListener) {
            this.onFormValueChangedListener.onFormValueChanged(element.getIndex(), element);
        }
    }

    private FormAdapter.FormMapper formMapper = new FormAdapter.FormMapper() {
        @Override
        public int getTotalCount() {
            return builder.getSize();
        }

        @Override
        public int getViewType(int position) {
            return builder.getElementType(position);
        }

        @Override
        public BaseCell createViewHolder(ViewGroup parent, int type) {
            return builder.createFormCell(parent, type);
        }

        @Override
        public void bindView(BaseCell cell, int position) {
            builder.bindFormCell(cell, position);
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
     * This listener is used to indicate that an input value was clicked in the {@link Form}.
     */
    public interface OnFormClickListener {
        /**
         * Is invoked whenever the user clicked on an TextCell in the form.
         *
         * @param position Relative position of the element in the form where the user has clicked.
         */
        void onFormClick(int position);
    }

    /**
     * This listener is used to indicate that an input value was long clicked.
     */
    public interface OnFormLongClickListener {
        /**
         * Is invoked whenever the TextCell section was long clicked.
         *
         * @param position Relative position of the element in the form where the user has clicked.
         * @return Whether the event was consumed.
         */
        boolean onFormLongClick(int position);
    }

    /**
     * This listener is used to indicate that an input value has been changed.
     */
    public interface OnFormValueChangedListener {
        /**
         * Is invoked whenever an TextCell had his value changed.
         *
         * @param position Relative position of the element in the form where the value was changed.
         * @param element Element in which the value was changed. Cast the element to the appropriate type using getType()
         *                And then use getValue() to get the most recent contents.
         */
        void onFormValueChanged(int position, BaseElement element);
    }

    FormAdapter.OnFormContextRequestListener contextRequestListener = this::getContext;
}
