package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;

public class MessageCell extends BaseCell {
    private TextView message;

    public MessageCell(@NonNull View itemView) {
        super(itemView);
        this.message = itemView.findViewById(R.id.message);
    }

    @Override
    public void reload() {
        message.setText(null);
    }

    public void setMessage(String msg, Form.FormMessage type) {
        message.setText(msg);

        if (type == Form.FormMessage.ALERT) {
            message.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
        } else {
            message.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
        }
    }
}
