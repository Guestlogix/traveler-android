package com.guestlogix.traveleruikit.adapters;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.BookingOption;
import com.guestlogix.traveleruikit.R;

import java.util.List;

public class OptionsAdapter extends RadioAdapter<BookingOption> {
    public OptionsAdapter(Context context, List<BookingOption> options) {
        super(context, options);
    }


    @Override
    public void onBindViewHolder(RadioAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);

        BookingOption option = mItems.get(i);

        viewHolder.mLabel.setText(option.getValue());
        if (option.getDisclaimer() != null) {
            viewHolder.mDisclaimer.setText(Html.fromHtml(option.getDisclaimer()));
        }
    }
}
