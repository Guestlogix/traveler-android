package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.FormFieldType;
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
        title = itemView.findViewById(R.id.title);
        subtitle = itemView.findViewById(R.id.subtitle);
    }

    /**
     * Expects a {@link HeaderFormModel} for binding.
     *
     * @param model description of the cell.
     */
    @Override
    public void bindWithModel(@NonNull FormModel model) {
        if (model.getType() != FormFieldType.HEADER) {
            TravelerLog.e("Expecting HeaderFormModel but got " + model.getClass().getName());
            return;
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
    public void setMessage(@Nullable FormMessage message) {
        // Do nothing.
    }
}
