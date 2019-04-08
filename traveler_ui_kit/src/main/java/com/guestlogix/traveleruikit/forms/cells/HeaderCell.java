package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.HeaderFormModel;

/**
 * Header cell. Does not implement any listeners.
 */
public class HeaderCell extends BaseCell {
    private TextView title;
    private TextView subtitle;

    public HeaderCell(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.headerTitle);
        subtitle = itemView.findViewById(R.id.headerSubtitle);
    }

    @Override
    public void setModel(@NonNull FormModel model) {
        if (!(model instanceof HeaderFormModel)) {
            throw new RuntimeException("Expecting HeaderFormModel but got " + model.getClass().getName());
        }

        HeaderFormModel h = (HeaderFormModel) model;

        if (h.getTitle() != null) {
            title.setText(h.getTitle());
        } else {
            title.setVisibility(View.GONE);
        }

        if (h.getSubtitle() != null) {
            subtitle.setText(h.getSubtitle());
        } else {
            subtitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void reload() {
        title.setText(null);
        subtitle.setText(null);
    }
}
