package com.guestlogix.traveleruikit.forms.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.TravelerLog;
import com.guestlogix.traveleruikit.forms.cells.*;
import com.guestlogix.traveleruikit.forms.descriptors.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder class which holds the underlying structure of the form.
 * <p>
 * How to use:
 * 1.   If you want to use any of your own views and classes, first register them with the builder. It will return to you
 * a type id which you will be then able to use to appropriately display your views.
 * <p>
 * 2.   Add the a form element one by one to the builder until the structure is built. The form will automatically inflate
 * all views as they are needed.
 * <p>
 * 3.   Pass the builder to the Form class to start rendering.
 * <p>
 * 4.   Do not subscribe to any of the listeners in the builder.
 * <p>
 * All available view types can be seen in {@link FormType}.
 */
@SuppressLint("UseSparseArrays")
public class FormBuilder {
    private final Context context;
    private Map<Integer, CustomCellAdapter> customComponentMap = new HashMap<>();
    private static int FORM_TYPE_COUNT;

    static {
        FORM_TYPE_COUNT = FormType.getTypeCount();
    }

    private int customCellCount;

    public FormBuilder(@NonNull Context context) {
        this.context = context;
        customCellCount = 0;
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

    public int getType(FormType type) {
        return type.getValue();
    }

    public BaseCell createFormCell(ViewGroup parent, int elementType) {
        switch (FormType.valueOf(elementType)) {
            case HEADER:
                return inflateHeader(parent);
            case BUTTON:
                return inflateButton(parent);
            case TEXT:
                return inflateText(parent);
            case QUANTITY:
                return inflateQuantity(parent);
            case SPINNER:
                return inflateSpinner(parent);
            default:
                return createCustomCell(parent, elementType);
        }
    }

    public void bindView(BaseCell cell, InputDescriptor descriptor, int type) {
        switch (FormType.valueOf(type)) {
            case SPINNER:
                bindSpinnerCell(cell, descriptor, ds);
                break;
            case QUANTITY:
                bindQuantityCell(cell, descriptor, ds);
                break;
            case TEXT:
                bindTextCell(cell, descriptor, ds);
                break;
            case BUTTON:
                bindButtonCell(cell, descriptor);
                break;
            default:
                bindCustomCell(cell, descriptor, type);
                break;
        }
    }

    /**
     * Binds a spinner cell with the appropriate values.
     *
     * @param cell       Cell of type {@link SpinnerCell} to be bound.
     * @param descriptor Expects an input descriptor of type {@link SpinnerDescriptor}.
     */
    public void bindSpinnerCell(BaseCell cell, InputDescriptor descriptor, BuilderDataSource ds) {
        SpinnerDescriptor s = (SpinnerDescriptor) descriptor;
        SpinnerCell c = (SpinnerCell) cell;

        c.reload();

        if (s.title != null) {
            c.setTitle(s.title);
        }

        if (s.options != null) {
            if (ds.getValue() != null) {
                c.setOptions(s.options, (Integer) ds.getValue());
            } else {
                c.setOptions(s.options);
            }
        }

        c.setError(ds.getError());
    }

    /**
     * Binds a text cell with the appropriate values.
     *
     * @param cell       Cell of type {@link TextCell} to be bound.
     * @param descriptor Expects an input descriptor of type {@link TextDescriptor}.
     */
    public void bindTextCell(BaseCell cell, InputDescriptor descriptor, BuilderDataSource ds) {
        TextDescriptor t = (TextDescriptor) descriptor;
        TextCell c = (TextCell) cell;

        c.reload();

        if (t.hint != null) {
            c.setHint(t.hint);
        }

        c.setValue((String) ds.getValue());

        c.setError(ds.getError());

        if (t.info != null) {
            c.setInfo(t.info);
        }
    }

    /**
     * Binds a quantity cell with the appropriate values.
     *
     * @param cell       Cell of type {@link QuantityCell} to be bound.
     * @param descriptor Expects an input descriptor of type {@link QuantityDescriptor}.
     */
    public void bindQuantityCell(BaseCell cell, InputDescriptor descriptor, BuilderDataSource ds) {
        QuantityDescriptor q = (QuantityDescriptor) descriptor;
        QuantityCell c = (QuantityCell) cell;

        if (q.title != null) {
            c.setTitle(q.title);
        }

        if (q.subtitle != null) {
            c.setSubtitle(q.subtitle);
        }

        c.setQuantity(String.valueOf(ds.getValue()));

        c.setAdapter(new QuantityCell.QuantityCellAdapter() {
            @Override
            public String getTitle() {
                return q.title;
            }

            @Nullable
            @Override
            public Integer getMaxQuantity() {
                return q.maxQuantity;
            }

            @NonNull
            @Override
            public Integer getMinQuantity() {
                return null != q.minQuantity ? q.minQuantity : 0;
            }

            @NonNull
            @Override
            public Integer getValue() {
                return ds.getValue() != null ? (Integer) ds.getValue() : q.minQuantity;
            }
        });
    }

    /**
     * Binds a button cell with the appropriate values.
     *
     * @param cell Cell of type {@link ButtonCell} to be bound.
     * @param descriptor Expects an input descriptor of type {@link ButtonDescriptor}.
     */
    public void bindButtonCell(BaseCell cell, InputDescriptor descriptor) {
        ButtonDescriptor b = (ButtonDescriptor) descriptor;
        ButtonCell c = (ButtonCell) cell;

        if (b.title != null) {
            c.setTitle(b.title);
        }

        c.setText(b.text);
    }

    /**
     * Binds a header cell with the appropriate values.
     * @param cell Cell of {@link HeaderCell} to be bound.
     * @param title Title of the header.
     * @param disclaimer Disclaimer for the header
     */
    public void bindHeaderCell(BaseCell cell, String title, String disclaimer) {
        HeaderCell c = (HeaderCell) cell;

        c.setTitle(title);
        c.setSubtitle(disclaimer);
    }

    public void bindCustomCell(BaseCell cell, InputDescriptor descriptor, int type) {

        if (customComponentMap.containsKey(type)) {
            CustomCellAdapter adapter = customComponentMap.get(type);

            if (null != adapter) {
                adapter.bindCell(cell, descriptor, type);
            } else {
                TravelerLog.e("CustomCellAdapter is null, cannot proceed with view binding");
            }
        } else {
            TravelerLog.e("Requested custom type %d was not registered with the builder", type);
        }
    }

    private BaseCell inflateHeader(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(FormLayout.HEADER_LAYOUT, parent, false);
        return new HeaderCell(view);
    }

    private BaseCell inflateButton(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(FormLayout.BUTTON_LAYOUT, parent, false);
        return new ButtonCell(view);
    }

    private BaseCell inflateText(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(FormLayout.TEXT_LAYOUT, parent, false);
        return new TextCell(view);
    }

    private BaseCell inflateQuantity(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(FormLayout.QUANTITY_LAYOUT, parent, false);
        return new QuantityCell(view);
    }

    private BaseCell inflateSpinner(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(FormLayout.SPINNER_LAYOUT, parent, false);
        return new SpinnerCell(view);
    }

    private BaseCell createCustomCell(ViewGroup parent, int type) {
        CustomCellAdapter adapter = customComponentMap.get(type);

        if (null != adapter) {
            return adapter.inflateCustomCell(context, parent);
        }

        TravelerLog.e("Cell type %d is not registered with the form builder.", type);
        throw new RuntimeException("Invalid cell type. Please make sure any custom types have been registered with the FormBuilder");
    }

    /**
     * Adapter interface used to create new FormCells and corresponding model Elements.
     */
    public interface CustomCellAdapter {
        /**
         * Inflates a custom view and binds it to its associated ViewHolder of type BaseCell.
         *
         * @param context Where to inflate.
         * @param parent  Parent element to attach to.
         * @return Cell to add to the tree.
         */
        BaseCell inflateCustomCell(Context context, ViewGroup parent);

        void bindCell(BaseCell cell, InputDescriptor descriptor, int type);
    }

    public interface BuilderDataSource {
        @Nullable
        Object getValue();

        @Nullable
        String getError();
    }
}

