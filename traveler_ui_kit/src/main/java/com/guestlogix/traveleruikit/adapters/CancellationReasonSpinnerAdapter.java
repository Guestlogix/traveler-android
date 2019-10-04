package com.guestlogix.traveleruikit.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.models.CancellationReason;

import java.util.List;

public class CancellationReasonSpinnerAdapter extends ArrayAdapter<CancellationReason> {


    public CancellationReasonSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<CancellationReason> cancellationReasons) {
        super(context, resource, cancellationReasons);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(getItem(position).getValue());

        return label;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getValue());

        return view;
    }
}
