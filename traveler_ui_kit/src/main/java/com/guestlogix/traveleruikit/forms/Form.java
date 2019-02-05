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
import com.guestlogix.traveleruikit.forms.adapters.FormMappingAdapter;
import com.guestlogix.traveleruikit.forms.cells.FormCell;

import java.util.ArrayList;
import java.util.List;

/**
 * A form layout used to display a list of sections each containing a set of questions.
 */
public class Form extends FrameLayout {

    private RecyclerView formCells;
    private RecyclerView.LayoutManager layoutManager;
    private FormMappingAdapter adapter;
    private FormStrategy formStrategy;

    private List<Pair<Integer, Integer>> indexMap;

    /**
     * Callback interface used to notify any subscriber whenever a click was performed on an InputCell in the form.
     */
    protected OnFormClickListener onFormClickListener;

    /**
     * Callback interface used to notify any subscriber whenever a long click was performed on an InputCell in the form.
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

    /**
     * Sets the strategy used to display the form.
     * The {@link FormStrategy} provides an interface used to determine how to display the form.
     *
     * @param strategy used to instantiate the form.
     */
    public void setFormStrategy(FormStrategy strategy) {
        this.formStrategy = strategy;
        setUpIndexMap();

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new FormMappingAdapter(formMapper);

        formCells.setLayoutManager(layoutManager);
        formCells.setAdapter(adapter);
    }

    public void reload() {

    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_form, this, true);
            formCells = view.findViewById(R.id.sections);
        }
    }

    private void setUpIndexMap() {
        indexMap = new ArrayList<>();
        int sections = formStrategy.getSectionCount();

        for (int i = 0; i < sections; i++) {
            indexMap.add(new Pair<>(i, -1));
            int inputs = formStrategy.getSectionInputCount(i);

            for (int j = 0; j < inputs; j++) {
                indexMap.add(new Pair<>(i, j));
            }
        }
    }

    /**
     * Callback interface responsible for defining the instantiation of the form.
     */
    public interface FormStrategy {
        /**
         * Binds a cell which contains the header type.
         *
         * @param sectionId section index.
         */
        void bindSection(int sectionId, FormCell sectionCell);

        /**
         * Binds a cell which contains an input.
         *
         * @param sectionId SectionCell Index.
         * @param inputId   InputCell index within the SectionCell.
         * @param inputCell
         */
        void bindInput(int sectionId, int inputId, FormCell inputCell);

        /**
         * Creates a FormCell based on the given type.
         *
         * @param parent   Root where the view should be created at.
         * @param viewType Integer representing the type
         * @return Cell holding the view.
         */
        FormCell createView(ViewGroup parent, int viewType);

        /**
         * Determines the type of the input cell based on the section and input indices combination.
         *
         * @param sectionId SectionCell index.
         * @param inputId   InputCell index within the section.
         * @return Type of the given input cell
         */
        int getInputType(int sectionId, int inputId);

        /**
         * Determines the type of the current section cell based on the section index.
         *
         * @param sectionId SectionCell index.
         * @return Type of current section cell.
         */
        int getSectionType(int sectionId);

        /**
         * How many sections are there in this form.
         *
         * @return Amount of sections in the form.
         */
        default int getSectionCount() {
            return 0;
        }

        /**
         * How many inputs there is in total in this section.
         *
         * @param sectionId SectionCell index where to look.
         * @return Amount of inputs in the given section.
         */
        default int getSectionInputCount(int sectionId) {
            return 0;
        }

    }

    FormMappingAdapter.FormMapper formMapper = new FormMappingAdapter.FormMapper() {
        @Override
        public int getTotalCount() {
            return null != indexMap ? indexMap.size() : 0;
        }

        @Override
        public int getViewType(int position) {
            if (null != formStrategy) {
                Pair<Integer, Integer> pair = indexMap.get(position);

                if (pair.second >= 0) {
                    return formStrategy.getInputType(pair.first, pair.second);
                } else {
                    return formStrategy.getSectionType(pair.first);
                }
            }
            return 0;
        }

        @Override
        public FormCell createViewHolder(ViewGroup parent, int type) {
            if (null != formStrategy) {
                return formStrategy.createView(parent, type);
            }
            return null;
        }

        @Override
        public void bindView(FormCell cell, int position) {
            if (null != formStrategy) {
                Pair<Integer, Integer> pair = indexMap.get(position);

                if (pair.second >= 0) {
                    formStrategy.bindInput(pair.first, pair.second, cell);
                } else {
                    formStrategy.bindSection(pair.first, cell);
                }
            }
        }

        @Override
        public void onClick(int position) {
            if (null != onFormClickListener) {
                Pair<Integer, Integer> pair = indexMap.get(position);
                onFormClickListener.onFormClick(pair.first, pair.second);
            }
        }

        @Override
        public boolean onLongClick(int position) {
            Pair<Integer, Integer> pair = indexMap.get(position);
            return null != onFormLongClickListener && onFormLongClickListener.onFormLongClick(pair.first, pair.second);
        }

        @Override
        public void onTextChanged(int position, CharSequence s) {
            if (null != onFormValueChangedListener) {
                Pair<Integer, Integer> pair = indexMap.get(position);

                onFormValueChangedListener.onFormValueChanged(pair.first, pair.second, s);
            }
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
         * Is invoked whenever the user clicked on an InputCell in the form.
         *
         * @param sectionId SectionCell index where the click was done.
         * @param inputId   InputCell index within the section where the click was done.
         */
        void onFormClick(int sectionId, int inputId);
    }

    /**
     * This listener is used to indicate that an input value was long clicked.
     */
    public interface OnFormLongClickListener {
        /**
         * Is invoked whenever the InputCell section was long clicked.
         *
         * @param sectionId SectionCell index where the long click was performed.
         * @param inputId   InputCell index within the section where the long click was performed.
         * @return Whether the event was consumed.
         */
        boolean onFormLongClick(int sectionId, int inputId);
    }

    /**
     * This listener is used to indicate that an input value has been changed.
     */
    public interface OnFormValueChangedListener {
        /**
         * Is invoked whenever an InputCell had his value changed.
         *
         * @param sectionId SectionCell index where the value was changed.
         * @param inputId   InputCell index within the section where the value was changed.
         * @param newValue  Current value of the input.
         */
        void onFormValueChanged(int sectionId, int inputId, CharSequence newValue);
    }
}
