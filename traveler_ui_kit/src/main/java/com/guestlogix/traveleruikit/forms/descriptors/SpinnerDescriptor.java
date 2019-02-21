package com.guestlogix.traveleruikit.forms.descriptors;

import java.util.List;

public class SpinnerDescriptor extends InputDescriptor {
    /**
     * Options that will be added to the dropdown selection.
     */
    public List<String> options;

    /**
     * Current option position selected.
     * Leave null, if no options were selected yet.
     */
    public Integer value;

    /**
     * Error for the descriptor.
     */
    public String error;
}
