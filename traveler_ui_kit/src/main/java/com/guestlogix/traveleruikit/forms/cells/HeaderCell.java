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
        title.setText(null);
        subtitle.setText(null);
    }

    public void setTitle(String title) {
        if (title == null) {
            this.title.setVisibility(View.GONE);
        } else {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(title);
        }
    }

    public void setSubtitle(String subtitle) {
        if (subtitle == null) {
            this.subtitle.setVisibility(View.GONE);
        } else {
            this.subtitle.setVisibility(View.VISIBLE);
            this.subtitle.setText(subtitle);
        }
    }

    private void init() {
        title = itemView.findViewById(R.id.headerTitle);
        subtitle = itemView.findViewById(R.id.headerSubtitle);
    }
}
