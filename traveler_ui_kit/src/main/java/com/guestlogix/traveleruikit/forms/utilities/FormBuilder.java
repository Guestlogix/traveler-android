package com.guestlogix.traveleruikit.forms.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.*;
import com.guestlogix.traveleruikit.forms.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormBuilder {
    private final Context context;

    private final List<BaseElement> elements;
    private Map<Integer, CustomCellAdapter> customComponentMap = new HashMap<>();
    private static int FORM_TYPE_COUNT;

    static {
        FORM_TYPE_COUNT = FormType.getTypeCount();
    }

    private int customCellCount;

    public FormBuilder(Context context) {
        this.context = context;
        elements = new ArrayList<>();
        customCellCount = 0;
    }

    /**
     * Adds a custom View instantiation logic for the
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
     * Adds a custom element to the tree.
     * @param elementType
     */
    public BaseElement addElement(int elementType) {
        if (elementType < FORM_TYPE_COUNT) {
            return addExistingElement(elementType);
        } else {
            return addCustomElement(elementType);
        }
    }

    public BaseElement addElement(FormType elementType) {
        return addExistingElement(elementType.getValue()); // TODO: Remove unnecessary conversion.
    }

    public int getElementType(int position) {
        return elements.get(position).getType().getValue();
    }

    public FormCell createFormCell(ViewGroup parent, int elementType) {
        switch (FormType.valueOf(elementType)) {
            case HEADER:
                return inflateHeader(parent);
            case BUTTON:
                return inflateButton(parent);
            case TEXT:
                return inflateText(parent);
            case QUANTITY:
                return inflateQuantity(parent);
            default:
                return createCustomCell(parent, elementType);
        }
    }

    public void bindFormCell(FormCell cell, int position) {
        BaseElement element = elements.get(position); // Element associated with the current view holder.
        element.updateCell(cell);
    }

    public int getSize() {
        return elements.size();
    }

    private FormCell inflateHeader(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(FormLayout.HEADER_LAYOUT, parent);
        return new HeaderCell(view);
    }

    private FormCell inflateButton(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(FormLayout.BUTTON_LAYOUT, parent);
        return new ButtonCell(view);
    }

    private FormCell inflateText(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(FormLayout.TEXT_LAYOUT, parent);
        return new TextCell(view);
    }

    private FormCell inflateQuantity(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(FormLayout.QUANTITY_LAYOUT, parent);
        return new QuantityCell(view);
    }

    private FormCell createCustomCell(ViewGroup parent, int type) {
        CustomCellAdapter adapter = customComponentMap.get(type);

        if (null != adapter) {
            BaseElement element = adapter.createCustomElement();
            element.setType(type);
            elements.add(element);
            return adapter.inflateCustomCell(context, parent);
        }

        throw new RuntimeException("Invalid cell type. Please make sure any custom types have been registered with the FormBuilder");
    }

    private void onValueCHnaged(BaseElement e) {
//        callback(int, int, e)
    }

    private BaseElement addExistingElement(int type) {
        BaseElement element = null;
        switch (FormType.valueOf(type)) {
            case TEXT:
                element = new TextElement();
                elements.add(element);
                break;
            case BUTTON:
                element = new ButtonElement();
                elements.add(element);
                break;
            case HEADER:
                element = new HeaderElement();
                elements.add(element);
                break;
            case QUANTITY:
                element = new QuantityElement();
                elements.add(element);
                break;
            default:
                throw new RuntimeException("Invalid cell type. Please make sure any custom types have been registered with the FormBuilder");
        }

        return element;
    }

    private BaseElement addCustomElement(int type) {
        CustomCellAdapter adapter = customComponentMap.get(type);

        if (null == adapter) {
            throw new RuntimeException("Invalid cell type. Please make sure any custom types have been registered with the FormBuilder");
        }

        BaseElement element = adapter.createCustomElement();
        element.setType(type);
        elements.add(element);

        return element;
    }

    /**
     * Adapter interface used to create new FormCells and corresponding model Elements.
     */
    public interface CustomCellAdapter {
        /**
         * Inflates a custom view and binds it to its associated ViewHolder of type FormCell.
         *
         * @param context Where to inflate.
         * @param parent  Parent element to attach to.
         * @return Cell to add to the tree.
         */
        FormCell inflateCustomCell(Context context, ViewGroup parent);

        /**
         * @return
         */
        BaseElement createCustomElement();
    }

}
