package com.guestlogix.traveleruikit.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.DateHelper;

import java.util.List;

public class TimeSlotSpinnerAdapter extends ArrayAdapter<Long> {

    public TimeSlotSpinnerAdapter(Activity context, int resourceId, @NonNull List<Long> list) {
        super(context, resourceId, list);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(DateHelper.formatTime(getItem(position)));

        return label;
    }
}
