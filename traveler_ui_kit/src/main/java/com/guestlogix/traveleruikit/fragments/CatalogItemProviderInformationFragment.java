package com.guestlogix.traveleruikit.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.ContactInfo;
import com.guestlogix.travelercorekit.models.Location;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.LabelValueRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CatalogItemProviderInformationFragment extends BaseFragment {

    private ContactInfo contactInfo;
    private List<Location> locationsList = new ArrayList<>();
    private static final String ARG_CONTACT_INFO = "contact_info";
    private static final String ARG_LOCATIONS_LIST = "locations_list";

    public static CatalogItemProviderInformationFragment getInstance(ContactInfo contactInfo, List<Location> locationsList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CONTACT_INFO, contactInfo);
        bundle.putSerializable(ARG_LOCATIONS_LIST, new ArrayList<>(locationsList));

        CatalogItemProviderInformationFragment fragment = new CatalogItemProviderInformationFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_catalog_item_provider_information, container, false);
        extractExtras();

        TextView providerNameTextView = mView.findViewById(R.id.providerNameTextView);
        TextView providerEmailTextView = mView.findViewById(R.id.providerEmailTextView);
        TextView providerWebsiteTextView = mView.findViewById(R.id.providerWebsiteTextView);

        providerNameTextView.setText(contactInfo.getName());
        providerEmailTextView.setText(contactInfo.getEmail());
        providerWebsiteTextView.setText(contactInfo.getWebsite());

        RecyclerView providerPhonesRV = mView.findViewById(R.id.providerPhonesRecyclerView);
        RecyclerView providerLocationsRV = mView.findViewById(R.id.providerLocationAddressesRecyclerView);

        providerPhonesRV.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        LabelValueRecyclerViewAdapter providerPhonesAdapter = new LabelValueRecyclerViewAdapter();
        providerPhonesAdapter.setMappingAdapter(new PhonesAdapter());
        providerPhonesRV.setAdapter(providerPhonesAdapter);

        providerLocationsRV.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        LabelValueRecyclerViewAdapter providerLocationsAdapter = new LabelValueRecyclerViewAdapter();
        providerLocationsAdapter.setMappingAdapter(new LocationsAdapter());
        providerLocationsRV.setAdapter(providerLocationsAdapter);

        return mView;
    }

    private void extractExtras() {
        Bundle bundle = this.getArguments();
        if (null != bundle) {
            contactInfo = (ContactInfo) bundle.getSerializable(ARG_CONTACT_INFO);
            locationsList = (List<Location>) bundle.getSerializable(ARG_LOCATIONS_LIST);
        }
    }

    private class PhonesAdapter implements LabelValueRecyclerViewAdapter.LabelValueMappingAdapter {
        @Override
        public void bindLabel(TextView label, int position) {
            label.setVisibility(View.GONE);
        }

        @Override
        public void bindValue(TextView value, int position) {
            value.setText(contactInfo.getPhones().get(position));
            value.setOnClickListener(onClickListener);
        }

        @Override
        public int getItemCount() {
            return contactInfo.getPhones().size();
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                String phoneNumber = contactInfo.getPhones().get(position);
                openDialer(phoneNumber);
            }
        };
    }

    private class LocationsAdapter implements LabelValueRecyclerViewAdapter.LabelValueMappingAdapter {
        @Override
        public void bindLabel(TextView label, int position) {
            label.setText(locationsList.get(position).getAddress());
        }

        @Override
        public void bindValue(TextView value, int position) {
            value.setText(getString(R.string.open_maps));
            value.setClickable(true);
            value.setOnClickListener(onClickListener);
        }

        @Override
        public int getItemCount() {

            return locationsList != null ? locationsList.size() : 0;
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                double lat = locationsList.get(position).getLatitude();
                double lon = locationsList.get(position).getLongitude();
                openMap(lat, lon);
            }
        };
    }

    private void openMap(double lat, double lon) {
        String uri = String.format(Locale.getDefault(), "geo:%f,%f", lat, lon);
        Uri gmmIntentUri = Uri.parse(uri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivityContext().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void openDialer(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
