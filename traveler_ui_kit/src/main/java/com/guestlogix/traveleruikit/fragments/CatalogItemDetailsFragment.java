package com.guestlogix.traveleruikit.fragments;


import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.traveleruikit.widgets.WrapContentViewPager;

public class CatalogItemDetailsFragment extends BaseFragment {

    private NestedScrollView mainNestedScrollView;
    private WrapContentViewPager catalogItemDetailsPager;
    private TabLayout catalogItemDetailsTabs;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;

    public CatalogItemDetailsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_item_details, container, false);

        mainNestedScrollView = view.findViewById(R.id.mainNestedScrollView);
        titleTextView = view.findViewById(R.id.titleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        imageView = view.findViewById(R.id.imageView);
        catalogItemDetailsPager = view.findViewById(R.id.catalogItemPager);
        catalogItemDetailsTabs = view.findViewById(R.id.catalogItemTabs);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CatalogItemDetailsViewModel catalogItemDetailsViewModel = ViewModelProviders.of(getActivityContext()).get(CatalogItemDetailsViewModel.class);
        catalogItemDetailsViewModel.getObservableCatalogItemDetails().observe(this, this::setView);

        FragmentTransaction fragmentTransaction = getActivityContext().getSupportFragmentManager().beginTransaction();
        ActionStripContainerFragment actionStripContainerFragment = new ActionStripContainerFragment();
        InformationSelectionContainerFragment infoSelectContainer = new InformationSelectionContainerFragment();

        fragmentTransaction.replace(R.id.action_container, actionStripContainerFragment);
        fragmentTransaction.replace(R.id.info_container, infoSelectContainer);

        fragmentTransaction.commit();
    }

    private void setView(CatalogItemDetails catalogItemDetails) {
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
                new ItemInformationTabsPagerAdapter(getActivityContext().getSupportFragmentManager(), getActivity());

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
    }

    private void focusOnView(View view) {
        mainNestedScrollView.post(() -> mainNestedScrollView.smoothScrollTo(0, view.getBottom() + 50));
    }
}
