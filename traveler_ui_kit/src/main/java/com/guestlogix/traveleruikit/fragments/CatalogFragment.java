package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogGroup;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.CatalogItemDetailsActivity;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.widgets.CatalogView;

import java.util.List;

import static com.guestlogix.traveleruikit.activities.CatalogItemDetailsActivity.ARG_CATALOG_ITEM;

/**
 * Fragment to hold the Catalog widget.
 */
//TODO: Give developer flexibility to change item clicks and see all clicks.
public class CatalogFragment extends BaseFragment {

    private static String ARG_CATALOG = "arg_catalog";

    private List<CatalogGroup> catalogGroups;
    private CatalogView catalogView;

    public CatalogFragment() {
        // Required empty public constructor
    }

    public static CatalogFragment newInstance(Catalog catalog) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CATALOG, catalog);
        CatalogFragment fragment = new CatalogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View catalogFragmentView = inflater.inflate(R.layout.fragment_catalog, container, false);
        catalogView = catalogFragmentView.findViewById(R.id.catalogView);

        Bundle args = getArguments();

        if (null != args && args.containsKey(ARG_CATALOG)) {
            catalogUpdateHandler((Catalog) args.getSerializable(ARG_CATALOG));
        }

        return catalogFragmentView;
    }

    private CatalogView.CatalogViewAdapter catalogViewAdapter = new CatalogView.CatalogViewAdapter() {
        @Override
        public void onBindSection(int sectionPosition, TextView titleTextView) {
            titleTextView.setText(catalogGroups.get(sectionPosition).getTitle());
        }

        @Override
        public void onBindItem(int sectionPosition, int itemIndex, int holderId, ImageView thumbNailImageView, TextView titleTextView) {
            CatalogItem item = catalogGroups.get(sectionPosition).getItems().get(itemIndex);

            //TODO: Set some default image
            thumbNailImageView.setImageResource(R.color.colorPrimary);
            AssetManager.getInstance().loadImage(item.getImageURL(),
                    (int) getResources().getDimension(R.dimen.thumbnail_width),
                    (int) getResources().getDimension(R.dimen.thumbnail_height),
                    holderId,
                    new ImageLoader.ImageLoaderCallback() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap) {
                            thumbNailImageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onError() {
                            thumbNailImageView.setImageResource(R.color.colorPrimary);
                        }
                    });
            titleTextView.setText(item.getTitle());
        }

        @Override
        public void onSeeAllClick(int sectionPosition) {
        }

        @Override
        public void onItemClick(int sectionPosition, int itemIndex) {
            CatalogItem catalogItem = catalogGroups.get(sectionPosition).getItems().get(itemIndex);

            Intent catalogItemDetailsIntent = new Intent(getActivityContext(), CatalogItemDetailsActivity.class);
            catalogItemDetailsIntent.putExtra(ARG_CATALOG_ITEM, catalogItem);
            startActivity(catalogItemDetailsIntent);
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

    private void catalogUpdateHandler(Catalog catalog) {
        if (null != catalog) {
            catalogGroups = catalog.getGroups();
            catalogView.setCatalogViewAdapter(catalogViewAdapter);
        }
    }
}
