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
    private TextView subTitle;

    @Override
    public void reload() {
        title.setText("");
        subTitle.setText("");
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setSubTitle(String subTitle) {
        this.subTitle.setText(subTitle);
    }

    private void init() {
        title = itemView.findViewById(R.id.headerTitle);
        subTitle = itemView.findViewById(R.id.headerSubtitle);
    }
}
