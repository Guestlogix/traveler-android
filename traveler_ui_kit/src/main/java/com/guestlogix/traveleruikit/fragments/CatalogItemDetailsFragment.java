package com.guestlogix.traveleruikit.fragments;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.traveleruikit.models.PurchaseContext;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.traveleruikit.widgets.WrapContentViewPager;

public class CatalogItemDetailsFragment extends Fragment {
    static public String ARG_CATALOG_ITEM_DETAILS = "ARG_CATALOG_ITEM_DETAILS";
    static private String TAG = "CatalogItemDetailsFragment";

    private WrapContentViewPager catalogItemDetailsPager;
    private ImageView imageView;

    public static CatalogItemDetailsFragment newInstance(CatalogItemDetails details) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATALOG_ITEM_DETAILS, details);
        CatalogItemDetailsFragment fragment = new CatalogItemDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_item_details, container, false);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_CATALOG_ITEM_DETAILS)) {
            Log.e(TAG, "No CatalogItemDetails in arguments");
            return null;
        }

        // TODO: Clean all of this up

        CatalogItemDetails catalogItemDetails = (CatalogItemDetails) args.get(ARG_CATALOG_ITEM_DETAILS);

        if (catalogItemDetails == null) {
            Log.e(TAG, "No CatalogItemDetails");
            return view;
        }

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        imageView = view.findViewById(R.id.imageView);
        catalogItemDetailsPager = view.findViewById(R.id.catalogItemPager);
        TabLayout catalogItemDetailsTabs = view.findViewById(R.id.catalogItemTabs);
//
//        ProductPurchaseContainerFragment purchaseContextContainer = (ProductPurchaseContainerFragment) getChildFragmentManager().
//                findFragmentById(R.id.fragment_catalogItemDetails_purchaseSelector);
//        purchaseContextContainer.setPurchaseContextChangedListener(this);
//

        ActionStripContainerFragment fragment = ActionStripContainerFragment.newInstance(catalogItemDetails);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        //transaction.replace(R.id.)

        titleTextView.setText(catalogItemDetails.getTitle());

        if (null != catalogItemDetails.getImageURLs() && catalogItemDetails.getImageURLs().size() > 0) {
            AssetManager.getInstance().loadImage(catalogItemDetails.getImageURLs().get(0),
                    (int) getResources().getDimension(R.dimen.thumbnail_width),
                    (int) getResources().getDimension(R.dimen.thumbnail_height),
                    imageView.getId(),
                    new ImageLoader.ImageLoaderCallback() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onError() {
                            imageView.setImageResource(R.color.colorPrimary);
                        }
                    });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionTextView.setText(Html.fromHtml(catalogItemDetails.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descriptionTextView.setText(Html.fromHtml(catalogItemDetails.getDescription()));
        }

        ItemInformationTabsPagerAdapter adapter =
                new ItemInformationTabsPagerAdapter(getFragmentManager(), getActivity());

        adapter.setContactInfo(catalogItemDetails.getContact());
        adapter.setInformationList(catalogItemDetails.getInformation());
        adapter.setLocationsList(catalogItemDetails.getLocations());
        catalogItemDetailsPager.setAdapter(adapter);
        catalogItemDetailsPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                catalogItemDetailsPager.requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        adapter.notifyDataSetChanged();

        catalogItemDetailsTabs.setupWithViewPager(catalogItemDetailsPager);


        return view;
    }

}
