package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.FormFieldType;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.MessageFormModel;

public class MessageCell extends BaseCell {
    private TextView message;

    public MessageCell(@NonNull View itemView) {
        super(itemView);
        this.message = itemView.findViewById(R.id.message);
    }

    /**
     * Expects a {@link MessageFormModel} for binding. Otherwise throws {@link RuntimeException}.
     *
     * @param model description of the cell.
     */
    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (model.getType() != FormFieldType.MESSAGE) {
            throw new RuntimeException("Expecting MessageFormModel, but got " + model.getClass().getName());
        }

        MessageFormModel m = (MessageFormModel) model;

        message.setText(m.getFormMessage().getMessage());

        if (m.getFormMessage().getType() == FormMessage.FormMessageType.ALERT) {
            message.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
        } else {
            message.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
        }
    }

    @Override
    public void setMessage(@Nullable FormMessage message) {
        // Is this recursion?
    }
}
