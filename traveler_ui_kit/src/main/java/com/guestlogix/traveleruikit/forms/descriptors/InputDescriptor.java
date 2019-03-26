package com.guestlogix.traveleruikit.forms.descriptors;

import androidx.annotation.Nullable;

import java.util.List;

public class InputDescriptor {
    /**
     * Title of the the cell. Will be used as the hint for edit texts.
     * Usually displayed on top of the actual input.
     */
    @Nullable
    public String title;

    /**
     * Subtitle of the cell.
     * Usually displayed under the title and is separate from the actual input.
     */
    @Nullable
    public String subtitle;

    /**
     * Options for the cell. Used in spinner type views.
     */
    @Nullable
    public List<String> options;
}
