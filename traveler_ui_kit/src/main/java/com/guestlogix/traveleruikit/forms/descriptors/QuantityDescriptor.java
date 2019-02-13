package com.guestlogix.traveleruikit.forms.descriptors;

public class QuantityDescriptor extends InputDescriptor {
    /**
     * Maximum value which can be picked in the number picker.
     * Leave null if unlimited maximum value is desired.
     */
    public Integer maxQuantity;

    /**
     * Minimum value which can be picked in the number picker.
     */
    public Integer minQuantity;

    /**
     * Current value in the picker.
     * Leave null if there is no value selected yet.
     */
    public Integer value;
}
