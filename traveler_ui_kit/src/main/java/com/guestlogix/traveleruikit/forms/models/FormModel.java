package com.guestlogix.traveleruikit.forms.models;

/**
 * Describes a field in the form.
 * Must use one of the available child classes.
 * <p>
 * See:
 * <ul>
 * <li>{@link ButtonFormModel}</li>
 * <li>{@link DateFormModel}</li>
 * <li>{@link HeaderFormModel}</li>
 * <li>{@link MessageFormModel}</li>
 * <li>{@link QuantityFormModel}</li>
 * <li>{@link SpinnerFormModel}</li>
 * <li>{@link TextFormModel}</li>
 * </ul>
 */
public abstract class FormModel {
    public abstract int getType();
}
