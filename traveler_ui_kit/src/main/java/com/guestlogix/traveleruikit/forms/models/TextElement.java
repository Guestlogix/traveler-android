package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.forms.cells.FormCell;
import com.guestlogix.traveleruikit.forms.cells.TextCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

public class TextElement extends BaseElement {
    public final static FormType TYPE = FormType.TEXT;
    private String hint;
    private CharSequence value;

    public TextElement(String title, String hint) {
        super(title);
        this.hint = hint;
    }

    public TextElement(String hint) {
        this.hint = hint;
    }

    public TextElement() {}

    @NonNull
    @Override
    public FormType getType() {
        return TYPE;
    }

    @Override
    public void setType(int type) {

    }

    @Override
    public void updateCell(FormCell cell) {
        TextCell tCell = (TextCell) cell;
        tCell.setHint(hint);
        tCell.setOnTextChangedListener(listener);
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    TextCell.OnTextChangedListener listener = (val) -> {
        value = val;

        if (null != this.onFormElementValueChangedListener) {
            this.onFormElementValueChangedListener.onValueChanged(this);
        }
    };
}
