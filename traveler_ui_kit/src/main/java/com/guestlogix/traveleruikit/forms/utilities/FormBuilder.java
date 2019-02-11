package com.guestlogix.traveleruikit.forms.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.*;
import com.guestlogix.traveleruikit.forms.listeners.OnFormElementClickListener;
import com.guestlogix.traveleruikit.forms.listeners.OnFormElementFocusChangedListener;
import com.guestlogix.traveleruikit.forms.listeners.OnFormElementValueChangedListener;
import com.guestlogix.traveleruikit.forms.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class FormBuilder {
    private final Context context;

    private final List<BaseElement> elements;
    private Map<Integer, CustomCellAdapter> customComponentMap = new HashMap<>();
    private static int FORM_TYPE_COUNT;

    private OnFormElementValueChangedListener onElementValueChangedListener;
    private OnFormElementClickListener onElementClickListener;
    private OnFormElementFocusChangedListener onFormElementFocusChangedListener;

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
     * Adds an element to the Form with type BUTTON.
     *
     * @param text Will be displayed on the button.
     */
    public void addButtonElement(String text) {
        ButtonElement element = new ButtonElement(text);
        setListeners(element);
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds an element to the form of type QUANTITY.
     *
     * @param title    Title to be displayed on the cell.
     * @param subtitle Subtitle to be displayed on the cell.
     * @param minValue Minimum value for the quantity picker. Default is 0.
     * @param maxValue Maximum value for the quantity picker. -1 for unlimited quantity.
     */
    public void addQuantityElement(String title, String subtitle, int minValue, int maxValue) {
        QuantityElement element = new QuantityElement(title, subtitle, minValue, maxValue);
        setListeners(element);
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds a blank element to the form of type QUANTITY.
     * Sets default values of 0 for min value and -1 for max value.
     */
    public void addQuantityElement() {
        QuantityElement element = new QuantityElement();
        setListeners(element);
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds an element to the form of type HEADER.
     *
     * @param title    Title to be displayed on the cell.
     * @param subtitle Subtitle to be displayed on the cell.
     */
    public void addHeaderElement(String title, String subtitle) {
        HeaderElement element = new HeaderElement(title, subtitle);
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds an element to the form of type HEADER.
     *
     * @param title Title to be displayed on the cell.
     */
    public void addHeaderElement(String title) {
        HeaderElement element = new HeaderElement(title);
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds a blank element to the form of type HEADER in {@link FormType} enum.
     */
    public void addHeaderElement() {
        HeaderElement element = new HeaderElement();
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds an element to the form of type TEXT.
     *
     * @param title Title to be displayed on the cell.
     * @param hint  Hint for the edit text.
     */
    public void addTextElement(String title, String hint) {
        TextElement element = new TextElement(title, hint);
        setListeners(element);
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds an element to the form of type TEXT.
     *
     * @param title Title to be displayed on the cell.
     */
    public void addTextElement(String title) {
        TextElement element = new TextElement(title);
        setListeners(element);
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds a blank element to the form of type TEXT.
     */
    public void addTextElement() {
        TextElement element = new TextElement();
        setListeners(element);
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds an element to the form of type SPINNER.
     *
     * @param title    Title to be displayed for this element.
     * @param subtitle Subtitle to be displayed for this element.
     * @param options  List of options to be used in the dropdown.
     */
    public void addSpinnerElement(String title, String subtitle, List<String> options) {
        SpinnerElement element = new SpinnerElement(title, subtitle, options);
        setListeners(element);
        element.setIndex(elements.size());
        elements.add(element);
    }

    /**
     * Adds an element to the form of type SPINNER with a default value.
     *
     * @param title        Title to be displayed for this element.
     * @param subtitle     Subtitle to be displayed for this element.
     * @param options      List of options to be used in the dropdown.
     * @param defaultValue Default value for the dropdown.
     */
    public void addSpinnerElement(String title, String subtitle, List<String> options, int defaultValue) {
        SpinnerElement element = new SpinnerElement(title, subtitle, options, defaultValue);
        setListeners(element);
        element.setIndex(elements.size());
        elements.add(element);
    }

    public BaseElement getElement(int position) {
        return elements.get(position);
    }

    /**
     * Adds a custom element to the tree.
     *
     * @param elementType
     */
    public BaseElement addElement(int elementType) {
        BaseElement element;
        if (elementType < FORM_TYPE_COUNT) {
            element = addExistingElement(elementType);
        } else {
            element = addCustomElement(elementType);
        }

        setListeners(element);
        return element;
    }

    public BaseElement addElement(FormType elementType) {
        return addExistingElement(elementType.getValue()); // TODO: Remove unnecessary conversion.
    }

    public int getElementType(int position) {
        return elements.get(position).getType();
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

    public void bindFormCell(BaseCell cell, int position) {
        BaseElement element = elements.get(position); // Element associated with the current view holder.
        element.updateCell(cell);
    }

    public int getSize() {
        return elements.size();
    }

    public void setOnElementValueChangedListener(OnFormElementValueChangedListener onElementValueChangedListener) {
        this.onElementValueChangedListener = onElementValueChangedListener;
    }

    public void setOnElementClickListener(OnFormElementClickListener onElementClickListener) {
        this.onElementClickListener = onElementClickListener;
    }

    public void setOnFormElementFocusChangedListener(OnFormElementFocusChangedListener onFormElementFocusChangedListener) {
        this.onFormElementFocusChangedListener = onFormElementFocusChangedListener;
    }

    /**
     * Signals to all elements in the form to reload.
     */
    public void reloadAll() {
        // Change min java api to 24 so we can use functional methods?
        for (BaseElement element : elements) {
            element.reload();
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

        throw new RuntimeException("Invalid cell type. Please make sure any custom types have been registered with the FormBuilder");
    }

    private void setListeners(BaseElement e) {
        e.setOnFormElementValueChangedListener(this::onValueChanged);
        e.setOnFormElementClickListener(this::onClick);
        e.setOnFormElementFocusChangedListener(this::onFocusChanged);
    }

    private void onValueChanged(BaseElement e) {
        if (null != onElementValueChangedListener) {
            onElementValueChangedListener.onValueChanged(e);
        }
    }

    private void onClick(BaseElement e) {
        if (null != onElementClickListener) {
            onElementClickListener.onFormElementClick(e);
        }
    }

    private void onFocusChanged(BaseElement e, boolean hasFocus) {
        if (null != onFormElementFocusChangedListener) {
            onFormElementFocusChangedListener.onFocusChanged(e, hasFocus);
        }
    }

    private BaseElement addExistingElement(int type) {
        BaseElement element;
        switch (FormType.valueOf(type)) {
            case TEXT:
                element = new TextElement();
                break;
            case BUTTON:
                element = new ButtonElement();
                break;
            case HEADER:
                element = new HeaderElement();
                break;
            case QUANTITY:
                element = new QuantityElement();
                break;
            case SPINNER:
                element = new SpinnerElement();
                break;
            default:
                throw new RuntimeException("Invalid cell type. Please make sure any custom types have been registered with the FormBuilder");
        }

        element.setIndex(elements.size());
        elements.add(element);
        return element;
    }

    private BaseElement addCustomElement(int type) {
        CustomCellAdapter adapter = customComponentMap.get(type);

        if (null == adapter) {
            throw new RuntimeException("Invalid cell type. Please make sure any custom types have been registered with the FormBuilder");
        }

        BaseElement element = adapter.createCustomElement();
        element.setType(type);
        element.setIndex(elements.size());
        elements.add(element);

        return element;
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

        /**
         * Instantiates the custom element extending {@link BaseElement}
         * @return Some custom element.
         */
        BaseElement createCustomElement();
    }
}
