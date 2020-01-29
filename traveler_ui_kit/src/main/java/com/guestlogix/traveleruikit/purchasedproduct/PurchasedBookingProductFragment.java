package com.guestlogix.traveleruikit.purchasedproduct;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.guestlogix.travelercorekit.callbacks.WishlistItemChangedCallback;
import com.guestlogix.travelercorekit.models.PurchasedBookingProduct;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.TermsAndConditionsActivity;
import com.guestlogix.traveleruikit.adapters.ImageURLAdapter;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.widgets.CarouselView;
import com.guestlogix.traveleruikit.widgets.WrapContentViewPager;

public class PurchasedBookingProductFragment extends Fragment {
    static String ARG_BOOKING_PURCHASED_PRODUCT = "ARG_BOOKING_PURCHASED_PRODUCT";
    private static String TAG = "BookingItemDetailsFragment";

    private WrapContentViewPager catalogItemDetailsPager;
    private ImageButton wishListToggleImageButton;
    private CarouselView carouselView;
    private PurchasedBookingProduct purchasedBookingProduct;
    private WishlistItemChangedCallback wishlistItemChangedCallback;

    public static PurchasedBookingProductFragment newInstance(PurchasedBookingProduct purchasedBookingProduct) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOKING_PURCHASED_PRODUCT, purchasedBookingProduct);
        PurchasedBookingProductFragment fragment = new PurchasedBookingProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchased_booking_details, container, false);

        Bundle args = getArguments();

        purchasedBookingProduct = (PurchasedBookingProduct) args.get(ARG_BOOKING_PURCHASED_PRODUCT);

        if (purchasedBookingProduct == null) {
            Log.e(TAG, "PurchasedBookingProduct in required to use this activity");
            return null;
        }


        TextView titleTextView = view.findViewById(R.id.textView_parking_details_title);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        catalogItemDetailsPager = view.findViewById(R.id.catalogItemPager);
        carouselView = view.findViewById(R.id.carouselView);
        TabLayout catalogItemDetailsTabs = view.findViewById(R.id.catalogItemTabs);

        titleTextView.setText(purchasedBookingProduct.getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionTextView.setText(Html.fromHtml(purchasedBookingProduct.getBookingItemDetails().getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descriptionTextView.setText(Html.fromHtml(purchasedBookingProduct.getBookingItemDetails().getDescription()));
        }

        if (null != purchasedBookingProduct.getBookingItemDetails().getImageUrls() && purchasedBookingProduct.getBookingItemDetails().getImageUrls().size() > 0) {
            RecyclerView.Adapter adapter = new ImageURLAdapter(purchasedBookingProduct.getBookingItemDetails().getImageUrls());
            carouselView.setAdapter(adapter);
        }

        ItemInformationTabsPagerAdapter adapter =
                new ItemInformationTabsPagerAdapter(getFragmentManager(), getActivity());
        adapter.setContactInfo(purchasedBookingProduct.getBookingItemDetails().getContact());
        adapter.setInformationList(purchasedBookingProduct.getBookingItemDetails().getInformation());
        adapter.setLocationsList(purchasedBookingProduct.getBookingItemDetails().getLocations());


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

        if (purchasedBookingProduct.getBookingItemDetails().getSupplier().getTrademark() != null) {
            TextView supplierTextView = view.findViewById(R.id.textView_catalogItemDetails_supplier);
            supplierTextView.setText(purchasedBookingProduct.getBookingItemDetails().getSupplier().getTrademark().getCopyright());

            ImageView imageView = view.findViewById(R.id.imageView_catalogItemDetails_supplier);
            AssetManager.getInstance().loadImage(purchasedBookingProduct.getBookingItemDetails().getSupplier().getTrademark().getIconURL(), imageView.getWidth(), imageView.getHeight(), imageView.getId(), new ImageLoader.ImageLoaderCallback() {
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
        termsAndConditionsButton.setVisibility(purchasedBookingProduct.getBookingItemDetails().getTermsAndConditions() == null ? View.INVISIBLE : View.VISIBLE);
        termsAndConditionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchasedBookingProductFragment.this.getContext(), TermsAndConditionsActivity.class);
                intent.putExtra(TermsAndConditionsActivity.ARG_CONTENT, purchasedBookingProduct.getBookingItemDetails().getTermsAndConditions());
                startActivity(intent);
            }
        });

        return view;
    }

}
