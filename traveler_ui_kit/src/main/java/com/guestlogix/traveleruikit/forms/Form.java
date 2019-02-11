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
 * <p>
 * Since the form takes a flat object structure, the implementing activity/fragment must have a flattening strategy.
 * <p>
 * All form events are not guaranteed to be invoked. It is up-to individual {@link BaseCell}s to implement event listeners
 * and dispatch the appropriate events.
 */
public class Form extends FrameLayout {

    private RecyclerView cellsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FormAdapter adapter;
    private FormBuilder builder;

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

    /**
     * Updates a view with a particular cell.
     *
     * @param e Element to update. Must have been instantiated by the {@link FormBuilder}
     */
    public void updateView(BaseElement e) {
        int pos = e.getIndex();
        BaseCell cell = (BaseCell) cellsRecyclerView.findViewHolderForLayoutPosition(pos);
        scrollToPosition(pos);
        e.updateCell(cell);
    }

    /**
     * Adds an error message to the cell. Up to individual cells to decide how this error looks.
     *
     * @param position Relative position of the cell in the form.
     * @param error    Error message to display.
     */
    public void setError(int position, String error) {
        BaseElement element = builder.getElement(position);
        BaseCell cell = (BaseCell) cellsRecyclerView.findViewHolderForLayoutPosition(position);
        element.setState(BaseElement.State.ERROR);
        element.setErrorMessage(error);
        scrollToPosition(position);
        element.updateCell(cell);
    }

    /**
     * Removes the error state from a cell and updates the view.
     *
     * @param position Position of the cell within the form to update.
     */
    public void removeError(int position) {
        BaseElement element = builder.getElement(position);

        if (element.getState() == BaseElement.State.ERROR) {
            BaseCell cell = (BaseCell) cellsRecyclerView.findViewHolderForLayoutPosition(position);
            element.setState(BaseElement.State.DEFAULT);
            scrollToPosition(position);
            element.updateCell(cell);
        }
    }

    /**
     * Adds an info message to the cell. Up to individual cells to decide how this looks.
     *
     * @param position relative position of the cell in the form.
     * @param info     Information to be displayed.
     */
    public void setInfo(int position, String info) {
        BaseCell cell = (BaseCell) cellsRecyclerView.findViewHolderForLayoutPosition(position);
        BaseElement element = builder.getElement(position);
        element.setState(BaseElement.State.INFO);
        element.setInfoMessage(info);
        scrollToPosition(position);
        element.updateCell(cell);
    }

    /**
     * Removes the info state from a cell and updates the view.
     *
     * @param position Position of the cell in the form to update.
     */
    public void removeInfo(int position) {
        BaseElement element = builder.getElement(position);

        if (element.getState() == BaseElement.State.DEFAULT) {
            BaseCell cell = (BaseCell) cellsRecyclerView.findViewHolderForLayoutPosition(position);
            element.setState(BaseElement.State.DEFAULT);
            scrollToPosition(position);
            element.updateCell(cell);
        }
    }

    /**
     * Scrolls to a particular position in the form.
     *
     * @param position Where to scroll to.
     */
    public void scrollToPosition(int position) {
        layoutManager.scrollToPosition(position);
    }

    /**
     * Uses the {@link FormBuilder} to initialize the form layout.
     *
     * @param builder
     */
    public void initialize(FormBuilder builder) {
        initialize(builder, new LinearLayoutManager(getContext()));
    }

    /**
     * Begins rendering the form using the data provided by the {@link FormBuilder}.
     *
     * @param builder
     * @param layoutManager
     */
    public void initialize(FormBuilder builder, RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        this.builder = builder;
        adapter = new FormAdapter(formMapper);
        adapter.setContextRequestListener(this.contextRequestListener);

        builder.setOnElementValueChangedListener(this::onElementValueChange);
        builder.setOnElementClickListener(this::onElementClick);
        builder.setOnFormElementFocusChangedListener(this::onElementFocusChange);

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

    private void onElementFocusChange(BaseElement element, boolean hasFocus) {
        if (null != this.onFormFocusChangedListener) {
            this.onFormFocusChangedListener.onFormFocusChanged(hasFocus, element);
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
         * Is invoked whenever there was a click on the cell.
         *
         * @param position Relative position of the element in the form where the user has clicked.
         */
        void onFormClick(int position);
    }

    /**
     * This listener is used to indicate that a cell was "long pressed"
     */
    public interface OnFormLongClickListener {
        /**
         * Is invoked whenever a cell was long clicked.
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
         * Is invoked whenever an cell had his value changed.
         *
         * @param position Relative position of the element in the form where the value was changed.
         * @param element  Element in which the value was changed. Cast the element to the appropriate type using getType()
         *                 And then use getValue() to get the most recent contents.
         */
        void onFormValueChanged(int position, BaseElement element);
    }

    /**
     * This listener is used to indicate that the focus has changed for a given input field.
     */
    public interface OnFormFocusChangedListener {
        /**
         * Is invoked whenever the focus state is changed for a given element of the form.
         *
         * @param hasFocus Whether the element now has focus.
         * @param element  Element in which the focus was changed.
         */
        void onFormFocusChanged(boolean hasFocus, BaseElement element);
    }

    FormAdapter.OnFormContextRequestListener contextRequestListener = this::getContext;
}
