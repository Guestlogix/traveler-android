package com.guestlogix.traveleruikit.forms.adapters;

import android.util.Log;
import com.guestlogix.traveleruikit.forms.Form;
import java.util.ArrayList;
import java.util.List;

public class SimpleFormAdapter extends Form.FormAdapter {
    private final static String TAG = "SimpleFormAdapter";
    private final List<Integer> types;

    public SimpleFormAdapter() {
        types = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    @Override
    public int getViewType(int position) {
        if (position > types.size() - 1) {
            Log.e(TAG, String.format("Position %d is not declared. Reverting to default type 0", position));
            return 0;
        }

        return types.get(position);
    }

    /**
     * Adds this type of object to the form.
     * @param type Form element type.
     */
    public void addFormField(int type) {
        types.add(type);
    }
}
