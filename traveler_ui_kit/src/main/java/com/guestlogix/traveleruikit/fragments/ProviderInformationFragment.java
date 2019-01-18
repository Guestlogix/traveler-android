package com.guestlogix.traveleruikit.fragments;

import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.LabelValueRecyclerViewAdapter;

public class ProviderInformationFragment extends Fragment {

    private View mView;

    private RecyclerView providerPhonesRV;
    private RecyclerView providerLocationsRV;

    private LabelValueRecyclerViewAdapter providerPhonesAdapter;
    private LabelValueRecyclerViewAdapter providerLocationsAdapter;

    public ProviderInformationFragment() {}

    public static ProviderInformationFragment getInstance() {// TODO: Add params
        ProviderInformationFragment fragment = new ProviderInformationFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_provider_information, container, false);
        setUp(mView);

        return mView;
    }

    private void setUp(View view) {
        providerPhonesRV = view.findViewById(R.id.providerPhonesRecyclerView);
        providerLocationsRV = view.findViewById(R.id.providerLocationAddressesRecyclerView);

        providerPhonesRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        providerPhonesAdapter = new LabelValueRecyclerViewAdapter();
        providerPhonesAdapter.setMappingAdapter(new PhonesAdapter());
        providerPhonesRV.setAdapter(providerPhonesAdapter);

        providerLocationsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        providerLocationsAdapter = new LabelValueRecyclerViewAdapter();
        providerLocationsAdapter.setMappingAdapter(new LocationsAdapter());
        providerLocationsRV.setAdapter(providerLocationsAdapter);
    }


    private class PhonesAdapter implements LabelValueRecyclerViewAdapter.LabelValueMappingAdapter {
        // TODO: Update with model.
        @Override
        public void bindLabel(TextView label, int position) {
            label.setText("303-394-6920");
            label.setOnClickListener(onClickListener);
        }

        @Override
        public void bindValue(TextView value, int position) {
            value.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    private class LocationsAdapter implements LabelValueRecyclerViewAdapter.LabelValueMappingAdapter {
        // TODO: Update with model.
        @Override
        public void bindLabel(TextView label, int position) {
            label.setText("Lisboa, Portugal, 1700-111");
        }

        @Override
        public void bindValue(TextView value, int position) {
            value.setText(getString(R.string.open_maps));
            value.setClickable(true);
            value.setOnClickListener(onClickListener);

            // Set style, add tag...
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

}
