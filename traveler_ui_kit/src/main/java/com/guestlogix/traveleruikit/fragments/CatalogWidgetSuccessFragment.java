package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import com.guestlogix.travelercorekit.models.CatalogGroup;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.CatalogWidgetViewModel;
import com.guestlogix.traveleruikit.widgets.CatalogView;

import java.util.List;

/**
 * Fragment to hold the Catalog widget.
 */
//TODO: Give developer flexibility to change item clicks and see all clicks.
public class CatalogWidgetSuccessFragment extends BaseFragment {

    private List<CatalogGroup> catalogGroups;
    private CatalogView catalogView;

    public CatalogWidgetSuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View catalogFragmentView = inflater.inflate(R.layout.fragment_catalog_widget_success, container, false);
        catalogView = catalogFragmentView.findViewById(R.id.catalogView);

        CatalogWidgetViewModel catalogWidgetViewModel = ViewModelProviders.of(getActivityContext()).get(CatalogWidgetViewModel.class);
        catalogWidgetViewModel.getGroupsObservable().observe(this, this::catalogUpdateHandler);

        return catalogFragmentView;
    }

    private CatalogView.CatalogViewAdapter catalogViewAdapter = new CatalogView.CatalogViewAdapter() {
        @Override
        public void onBindSection(int sectionPosition, TextView titleTextView) {
            titleTextView.setText(catalogGroups.get(sectionPosition).getTitle());
        }

        @Override
        public void onBindItem(int sectionPosition, int itemIndex, ImageView thumbNailImageView, TextView titleTextView, TextView subTitleTextView) {
            CatalogItem item = catalogGroups.get(sectionPosition).getItems().get(itemIndex);
            titleTextView.setText(item.getTitle());
            subTitleTextView.setText(item.getSubTitle());
        }

        @Override
        public void onSeeAllClick(int sectionPosition) {
        }

        @Override
        public void onItemClick(int sectionPosition, int itemIndex) {
            CatalogItem catalogItem = catalogGroups.get(sectionPosition).getItems().get(itemIndex);

            CatalogWidgetSuccessFragmentDirections.CatalogItemDetailsAction directions = CatalogWidgetSuccessFragmentDirections.catalogItemDetailsAction(catalogItem);
            Navigation.findNavController(catalogView).navigate(directions);

        }

        @Override
        public int getSectionsCount() {
            return catalogGroups.size();
        }

        @Override
        public int getSectionItemsCount(int sectionIndex) {
            return catalogGroups.get(sectionIndex).getItems().size();
        }
    };


    private void catalogUpdateHandler(List<CatalogGroup> catalogGroups) {
        this.catalogGroups = catalogGroups;
        catalogView.setCatalogViewAdapter(catalogViewAdapter);
    }

}
