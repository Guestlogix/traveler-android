package com.guestlogix.traveleruikit.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.guestlogix.travelercorekit.callbacks.WishlistAddCallback;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.TermsAndConditionsActivity;
import com.guestlogix.traveleruikit.adapters.ImageURLAdapter;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.widgets.CarouselView;
import com.guestlogix.traveleruikit.widgets.WrapContentViewPager;

import java.util.ArrayList;
import java.util.List;

public class CatalogItemDetailsFragment extends Fragment implements WishlistAddCallback{
    private static final String ARG_CATALOG_ITEM_DETAILS = "ARG_CATALOG_ITEM_DETAILS";
    private static final String TAG = "CatalogItemDetailsFrag";

    private WrapContentViewPager catalogItemDetailsPager;
    private CarouselView carouselView;
    private ImageButton wishListToggleImageButton;
    private CatalogItemDetails catalogItemDetails;

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

        catalogItemDetails = (CatalogItemDetails) args.get(ARG_CATALOG_ITEM_DETAILS);

        if (catalogItemDetails == null) {
            Log.e(TAG, "No CatalogItemDetails");
            return view;
        }

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        carouselView = view.findViewById(R.id.carouselView);
        catalogItemDetailsPager = view.findViewById(R.id.catalogItemPager);
        TabLayout catalogItemDetailsTabs = view.findViewById(R.id.catalogItemTabs);
//
//        ProductPurchaseContainerFragment purchaseContextContainer = (ProductPurchaseContainerFragment) getChildFragmentManager().
//                findFragmentById(R.id.fragment_catalogItemDetails_purchaseSelector);
//        purchaseContextContainer.setPurchaseContextChangedListener(this);
//
        titleTextView.setText(catalogItemDetails.getTitle());

        if (null != catalogItemDetails.getImageURLs() && catalogItemDetails.getImageURLs().size() > 0) {
            RecyclerView.Adapter adapter = new ImageURLAdapter(catalogItemDetails.getImageURLs());
            carouselView.setAdapter(adapter);
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

        if (catalogItemDetails.getSupplier().getTrademark() != null) {
            TextView supplierTextView = view.findViewById(R.id.textView_catalogItemDetails_supplier);
            supplierTextView.setText(catalogItemDetails.getSupplier().getTrademark().getCopyright());

            ImageView imageView = view.findViewById(R.id.imageView_catalogItemDetails_supplier);
            AssetManager.getInstance().loadImage(catalogItemDetails.getSupplier().getTrademark().getIconURL(), imageView.getWidth(), imageView.getHeight(), imageView.getId(), new ImageLoader.ImageLoaderCallback() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onError() {

                }
            });
        }

        Button termsAndConditionsButton = view.findViewById(R.id.button_catalogItemDetails_termsAndConditions);
        termsAndConditionsButton.setVisibility(catalogItemDetails.getTermsAndConditions() == null ? View.INVISIBLE : View.VISIBLE);
        termsAndConditionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogItemDetailsFragment.this.getContext(), TermsAndConditionsActivity.class);
                intent.putExtra(TermsAndConditionsActivity.ARG_CONTENT, catalogItemDetails.getTermsAndConditions());
                startActivity(intent);
            }
        });

        wishListToggleImageButton = view.findViewById(R.id.imagebutton_wishlist_toggle);
        wishListToggleImageButton.setSelected(false);
        wishListToggleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wishListToggleImageButton.isSelected()) {
                    //TODO remove wishlist
                } else {
                    Traveler.addToWishlist(catalogItemDetails, CatalogItemDetailsFragment.this);
                    wishListToggleImageButton.setSelected(true);
                }
            }
        });

        return view;
    }

    @Override
    public void onWishlistAddSuccess(CatalogItemDetails itemDetails) {
        catalogItemDetails = itemDetails;
    }

    @Override
    public void onWishlistAddError(Error error) {
        wishListToggleImageButton.setSelected(false);
    }
}
