package com.guestlogix.traveleruikit.fragments;


import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.LabelValueRecyclerViewAdapter;

import java.util.List;


/**
 * Displays a list of items which represent information for a particular object.
 */
public class ItemDetailInformationFragment extends Fragment {
    private View mView;
    private RecyclerView infoRecyclerView;
    private LabelValueRecyclerViewAdapter itemViewAdapter;
    private List<Object> objects;


    public ItemDetailInformationFragment() {}

    public static ItemDetailInformationFragment getInstance() { // TODO pass params;
        ItemDetailInformationFragment fragment = new ItemDetailInformationFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_item_detail_information, container, false);

        setUp(mView);

        return mView;
    }

    private void setUp(View view) {
        infoRecyclerView = view.findViewById(R.id.informationRecyclerView);

        infoRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        itemViewAdapter = new LabelValueRecyclerViewAdapter();
        itemViewAdapter.setMappingAdapter(new InformationAdapter());
    }

    private class InformationAdapter implements LabelValueRecyclerViewAdapter.LabelValueMappingAdapter {
        // TODO: Update with model.
        @Override
        public void bindLabel(TextView label, int position) {
            label.setText("Before Lmao");
        }

        @Override
        public void bindValue(TextView value, int position) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                value.setText(Html.fromHtml("Jan 13, 1970", Html.FROM_HTML_MODE_COMPACT));
            } else {
                value.setText(Html.fromHtml("Jan 13, 1970"));
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
