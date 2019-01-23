package com.guestlogix.traveleruikit.fragments;


import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.LabelValueRecyclerViewAdapter;

import java.util.ArrayList;


/**
 * Displays a list of items which represent information for a particular object.
 */
public class CatalogItemInformationFragment extends Fragment {
    private View mView;
    private RecyclerView infoRecyclerView;
    private LabelValueRecyclerViewAdapter itemViewAdapter;
    ArrayList<Attribute> itemInfoList;

    private static final String ARG_ITEM_INFO = "item_info";


    public CatalogItemInformationFragment() {
    }

    public static CatalogItemInformationFragment getInstance(ArrayList<Attribute> itemInfoList) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ITEM_INFO, itemInfoList);

        CatalogItemInformationFragment fragment = new CatalogItemInformationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_catalog_item_information, container, false);
        extractExtras();
        setUp(mView);

        return mView;
    }

    private void extractExtras() {

        Bundle bundle = this.getArguments();
        if (null != bundle) {
            itemInfoList = (ArrayList<Attribute>) bundle.getSerializable(ARG_ITEM_INFO);
        }
    }

    private void setUp(View view) {
        infoRecyclerView = view.findViewById(R.id.informationRecyclerView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        infoRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(infoRecyclerView.getContext(),
                (linearLayoutManager).getOrientation());
        infoRecyclerView.addItemDecoration(dividerItemDecoration);

        itemViewAdapter = new LabelValueRecyclerViewAdapter();
        itemViewAdapter.setMappingAdapter(informationAdapter);
        infoRecyclerView.setAdapter(itemViewAdapter);
    }

    LabelValueRecyclerViewAdapter.LabelValueMappingAdapter informationAdapter = new LabelValueRecyclerViewAdapter.LabelValueMappingAdapter() {
        @Override
        public void bindLabel(TextView label, int position) {
            label.setText(itemInfoList.get(position).getLabel());
        }

        @Override
        public void bindValue(TextView value, int position) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                value.setText(Html.fromHtml(itemInfoList.get(position).getValue(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                value.setText(Html.fromHtml(itemInfoList.get(position).getValue()));
            }
        }

        @Override
        public int getItemCount() {
            return itemInfoList.size();
        }
    };
}
