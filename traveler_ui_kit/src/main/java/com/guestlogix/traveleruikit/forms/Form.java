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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.adapters.FormRecyclerViewAdapter;
import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.forms.utilities.FormBuilder;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

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
    private final FormBuilder builder;
    private Map<Pair<Integer, Integer>, Integer> pairToPos;
    private Map<Integer, Pair<Integer, Integer>> posToPair;
    private Map<Integer, Integer> posToType;
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
        builder = new FormBuilder(getContext());
    }

    public Form(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, 0);
        builder = new FormBuilder(getContext());
    }

    public Form(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
        builder = new FormBuilder(getContext());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Form(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
        builder = new FormBuilder(getContext());
    }

    /**
     * Scrolls to a particular position in the form.
     */
    public void scrollToPosition(int sectionId, int fieldId) {
        Pair<Integer, Integer> p = new Pair<>(sectionId, fieldId);
        Integer pos = pairToPos.get(p);

        if (pos != null) {
            layoutManager.scrollToPosition(pos);
        }
    }

    public void scrollToPosition(int sectionId) {
        // Header should be
        Pair<Integer, Integer> p = new Pair<>(sectionId, -1);
        Integer pos = pairToPos.get(p);

        if (pos != null) {
            if (pos != 0) {
                layoutManager.scrollToPosition(pos);
            }
        }
    }

    /**
     * Reloads all elements in the form.
     */
    public void reload() {
        buildMappings();
        rvAdapter.notifyDataSetChanged();
    }

    /**
     * Notifies the form that an item needs to be updated.
     * The form will use the data set to update the specific item
     *
     * @param sectionId Section where to update.
     * @param fieldId   Item within the section to update.
     */
    public void updateField(int sectionId, int fieldId) {
        Integer pos = pairToPos.get(new Pair<>(sectionId, fieldId));

        if (null != pos) {
            rvAdapter.notifyItemChanged(pos);
        }
    }

    public void setDataSource(@NonNull DataSource dataSource, RecyclerView.LayoutManager layoutManager) {
        this.dataSource = dataSource;
        this.layoutManager = layoutManager;
        buildMappings();

        rvAdapter = new FormRecyclerViewAdapter(formMapper);
        rvAdapter.setContextRequestListener(contextRequestListener);
        rvAdapter.setCellEventsListener(cellEventsListener);

        cellsRecyclerView.setLayoutManager(this.layoutManager);
        cellsRecyclerView.setAdapter(rvAdapter);
        cellsRecyclerView.setItemAnimator(animator);
    }

    public void setDataSource(@NonNull DataSource dataSource) {
        setDataSource(dataSource, new LinearLayoutManager(getContext()));
    }

    public int getType(FormType type) {
        return builder.getType(type);
    }

    public FormBuilder getBuilder() {
        return builder;
    }

    public int registerCustomView(FormBuilder.CustomCellAdapter adapter) {
        return builder.registerCustomCell(adapter);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_form, this, true);
            cellsRecyclerView = view.findViewById(R.id.sections);
        }
    }

    private void buildMappings() {
        count = 0;
        posToPair = new HashMap<>();
        posToType = new HashMap<>();
        pairToPos = new HashMap<>();

        for (int i = 0; i < dataSource.getSectionCount(); i++) {
            Pair<Integer, Integer> p = new Pair<>(i, -1);
            posToPair.put(count, p);
            posToType.put(count, builder.getType(FormType.HEADER));
            count++;


            for (int j = 0; j < dataSource.getFieldCount(i); j++) {
                int type = dataSource.getType(i, j);
                posToType.put(count, type);
                p = new Pair<>(i, j);
                posToPair.put(count, p);
                pairToPos.put(p, count);
                count++;
            }

        }
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
            return builder.createFormCell(parent, type);
        }

        @Override
        public void bindView(BaseCell cell, int position) {
            Pair<Integer, Integer> p = posToPair.get(position);
            Integer type = posToType.get(position);

            if (null != p && null != type) {
                InputDescriptor descriptor;
                if (p.second >= 0) {
                    descriptor = dataSource.getDescriptor(p.first, p.second, type);
                    builder.bindView(cell, descriptor, type);
                } else {
                    builder.bindHeaderCell(cell, dataSource.getTitle(p.first), dataSource.getDisclaimer(p.first));
                }
            }
        }
    };

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

    public interface DataSource {
        int getSectionCount();

        int getFieldCount(int sectionId);

        int getType(int sectionId, int fieldId);

        String getTitle(int sectionId);

        String getDisclaimer(int sectionId);

        InputDescriptor getDescriptor(int sectionId, int fieldId, int type);


    }

    DefaultItemAnimator animator = new DefaultItemAnimator() {
        @Override
        public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
            return true;
        }
    };
}
