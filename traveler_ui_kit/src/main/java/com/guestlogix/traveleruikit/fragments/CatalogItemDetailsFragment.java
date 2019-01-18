package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogItemDetailsFragment extends Fragment {
    private View mView;

    private ViewPager catalogItemPager;
    private TabLayout catalogItemTabs;

    public CatalogItemDetailsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_catalog_item_details, container, false);
        setUp(mView);

        return mView;
    }

    private void setUp(View view) {
        catalogItemPager = view.findViewById(R.id.catalogItemPager);
        catalogItemTabs = view.findViewById(R.id.catalogItemTabs);
        PagerAdapter adapter = new ItemInformationTabsPagerAdapter(getActivity().getSupportFragmentManager());
        catalogItemPager.setAdapter(adapter);
        catalogItemTabs.setupWithViewPager(catalogItemPager);
    }

}
