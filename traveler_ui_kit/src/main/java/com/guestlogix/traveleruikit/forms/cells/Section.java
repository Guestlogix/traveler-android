package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class Section extends FormCell {
    private List<Input> inputs;

    public Section(@NonNull View itemView) {
        super(itemView);
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public void addInput(Input input) {
        if (null == inputs) {
            inputs = new ArrayList<>();
        }

        inputs.add(input);
    }
}
