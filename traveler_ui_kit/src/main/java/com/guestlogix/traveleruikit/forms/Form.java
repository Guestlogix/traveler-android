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
 * A flexible view to display forms.
 */
public class Form extends FrameLayout {

    private RecyclerView formCells;
    private FormManager formManager;

    private List<Pair<Integer, Integer>> indexMap;

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

    public void setAdapter(FormManager manager) {
        this.formManager = manager;
        setUpIndexMap();

        FormMappingAdapter adapter = new FormMappingAdapter();
        adapter.setFormMapper(formMapper);
        formCells.setLayoutManager(new LinearLayoutManager(getContext()));
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
        int sections = formManager.getSectionCount();

        for (int i = 0; i < sections; i++) {
            indexMap.add(new Pair<>(i, -1));
            int inputs = formManager.getSectionInputCount(i);

            for (int j = 0; j < inputs; j++) {
                indexMap.add(new Pair<>(i, j));
            }
        }
    }

    /**
     * Callback interface responsible for providing the instantiation logic.
     */
    public interface FormManager {
        /**
         * @param sectionId
         */
        void bindSection(int sectionId, FormCell sectionCell);

        void bindInput(int sectionId, int inputId, FormCell inputCell);

        FormCell createView(ViewGroup parent, int viewType);

        int getInputType(int sectionId, int inputId);

        int getSectionType(int sectionId);

        int getSectionCount();

        int getSectionInputCount(int sectionId);

    }

    FormMappingAdapter.FormMapper formMapper = new FormMappingAdapter.FormMapper() {
        @Override
        public int getTotalCount() {
            return null != indexMap ? indexMap.size() : 0;
        }

        @Override
        public int getViewType(int position) {
            if (null != formManager) {
                Pair<Integer, Integer> pair = indexMap.get(position);

                if (pair.second >= 0) {
                    return formManager.getInputType(pair.first, pair.second);
                } else {
                    return formManager.getSectionType(pair.first);
                }
            }
            return 0;
        }

        @Override
        public FormCell createViewHolder(ViewGroup parent, int type) {
            if (null != formManager) {
                return formManager.createView(parent, type);
            }
            return null;
        }

        @Override
        public void bindView(FormCell cell, int position) {
            if (null != formManager) {
                Pair<Integer, Integer> pair = indexMap.get(position);

                if (pair.second >= 0) {
                    formManager.bindInput(pair.first, pair.second, cell);
                } else {
                    formManager.bindSection(pair.first, cell);
                }
            }
        }
    };
}
