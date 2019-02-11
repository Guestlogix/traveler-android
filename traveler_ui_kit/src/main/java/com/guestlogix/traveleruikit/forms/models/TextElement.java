package com.guestlogix.traveleruikit.forms.models;

import com.guestlogix.traveleruikit.forms.cells.BaseCell;
import com.guestlogix.traveleruikit.forms.cells.TextCell;
import com.guestlogix.traveleruikit.forms.utilities.FormType;

/**
 * Form element containing information which can be used to update {@link BaseCell} with an edit text.
 * Implements:
 * {@link com.guestlogix.traveleruikit.forms.listeners.OnFormElementValueChangedListener}
 */
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

    public TextElement() {
    }

    @Override
    public int getType() {
        return TYPE.getValue();
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void updateCell(BaseCell cell) {
        TextCell tCell = (TextCell) cell;
        tCell.reload();
        tCell.setHint(hint);
        tCell.setOnTextChangedListener(listener);

        if (state == State.ERROR) {
            updateErrorState(tCell);
        }
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public CharSequence getValue() {
        return value;
    }

    private void updateErrorState(TextCell cell) {
        cell.setError(errorMessage);
    }

    private TextCell.OnTextChangedListener listener = (val) -> {
        value = val;

        if (null != this.onFormElementValueChangedListener) {
            this.onFormElementValueChangedListener.onValueChanged(this);
        }
    };
}