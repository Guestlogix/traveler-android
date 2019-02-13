package com.guestlogix.traveleruikit.forms.cells;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.traveleruikit.R;

/**
 * Header cell. Does not implement any listeners.
 */
public class HeaderCell extends BaseCell {
    public HeaderCell(@NonNull View itemView) {
        super(itemView);
        init();
    }

    private TextView title;
    private TextView subtitle;

    @Override
    public void reload() {
        title.setText("");
        subtitle.setText("");
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setSubtitle(String subTitle) {
        this.subtitle.setText(subTitle);
    }

    private void init() {
        title = itemView.findViewById(R.id.headerTitle);
        subtitle = itemView.findViewById(R.id.headerSubtitle);
    }
}
