package com.guestlogix.traveleruikit.fragments;

import android.app.AlertDialog;
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
import com.guestlogix.travelercorekit.callbacks.WishlistAddCallback;
import com.guestlogix.travelercorekit.callbacks.WishlistItemChangedCallback;
import com.guestlogix.travelercorekit.callbacks.WishlistRemoveCallback;
import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.BookingItemDetails;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.models.WishlistResult;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.TermsAndConditionsActivity;
import com.guestlogix.traveleruikit.adapters.ImageURLAdapter;
import com.guestlogix.traveleruikit.adapters.ItemInformationTabsPagerAdapter;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.widgets.CarouselView;
import com.guestlogix.traveleruikit.widgets.WrapContentViewPager;

public class BookingItemDetailsFragment extends Fragment implements WishlistAddCallback, WishlistRemoveCallback {
    static String ARG_BOOKING_ITEM = "ARG_BOOKING_ITEM";
    static String ARG_BOOKING_ITEM_DETAILS = "ARG_BOOKING_ITEM_DETAILS";
    private static String TAG = "BookingItemDetailsFragment";

    private WrapContentViewPager catalogItemDetailsPager;
    private CarouselView carouselView;
    private ImageButton wishListToggleImageButton;
    private BookingItemDetails bookingItemDetails;
    private BookingItem bookingItem;
    private WishlistItemChangedCallback wishlistItemChangedCallback;

    public static BookingItemDetailsFragment newInstance(BookingItem item, CatalogItemDetails details) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOKING_ITEM, item);
        args.putSerializable(ARG_BOOKING_ITEM_DETAILS, details);
        BookingItemDetailsFragment fragment = new BookingItemDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setWishlistItemChangedCallback(WishlistItemChangedCallback wishlistItemChangedCallback) {
        this.wishlistItemChangedCallback = wishlistItemChangedCallback;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_item_details, container, false);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_BOOKING_ITEM_DETAILS)) {
            Log.e(TAG, "No BookingItemDetails in arguments");
            return null;
        }

        if (!args.containsKey(ARG_BOOKING_ITEM)) {
            Log.e(TAG, "No Booking Item in arguments");
            return null;
        }

        // TODO: Clean all of this up

        bookingItem = (BookingItem) args.get(ARG_BOOKING_ITEM);
        bookingItemDetails = (BookingItemDetails) args.get(ARG_BOOKING_ITEM_DETAILS);

        if (bookingItemDetails == null) {
            Log.e(TAG, "No CatalogItemDetails");
            return view;
        }

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        carouselView = view.findViewById(R.id.carouselView);
        catalogItemDetailsPager = view.findViewById(R.id.catalogItemPager);
        TabLayout catalogItemDetailsTabs = view.findViewById(R.id.catalogItemTabs);
        titleTextView.setText(bookingItemDetails.getTitle());

        if (null != bookingItemDetails.getImageUrls() && bookingItemDetails.getImageUrls().size() > 0) {
            RecyclerView.Adapter adapter = new ImageURLAdapter(bookingItemDetails.getImageUrls());
            carouselView.setAdapter(adapter);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionTextView.setText(Html.fromHtml(bookingItemDetails.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descriptionTextView.setText(Html.fromHtml(bookingItemDetails.getDescription()));
        }

        ItemInformationTabsPagerAdapter adapter =
                new ItemInformationTabsPagerAdapter(getFragmentManager(), getActivity());

        adapter.setContactInfo(bookingItemDetails.getContact());
        adapter.setInformationList(bookingItemDetails.getInformation());
        adapter.setLocationsList(bookingItemDetails.getLocations());
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

        if (bookingItemDetails.getSupplier().getTrademark() != null) {
            TextView supplierTextView = view.findViewById(R.id.textView_catalogItemDetails_supplier);
            supplierTextView.setText(bookingItemDetails.getSupplier().getTrademark().getCopyright());

            ImageView imageView = view.findViewById(R.id.imageView_catalogItemDetails_supplier);
            AssetManager.getInstance().loadImage(bookingItemDetails.getSupplier().getTrademark().getIconURL(), imageView.getWidth(), imageView.getHeight(), imageView.getId(), new ImageLoader.ImageLoaderCallback() {
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
        termsAndConditionsButton.setVisibility(bookingItemDetails.getTermsAndConditions() == null ? View.INVISIBLE : View.VISIBLE);
        termsAndConditionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingItemDetailsFragment.this.getContext(), TermsAndConditionsActivity.class);
                intent.putExtra(TermsAndConditionsActivity.ARG_CONTENT, bookingItemDetails.getTermsAndConditions());
                startActivity(intent);
            }
        });

        wishListToggleImageButton = view.findViewById(R.id.imagebutton_wishlist_toggle);
        wishListToggleImageButton.setSelected(bookingItemDetails.isWishlisted());
        wishListToggleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookingItemDetails.isWishlisted()) {
                    //Traveler.addToWishlist(catalogItemDetails, CatalogItemDetailsFragment.this);
                    wishListToggleImageButton.setSelected(false);
                    Traveler.wishlistRemove(bookingItem, null, BookingItemDetailsFragment.this);
                } else {
                    wishListToggleImageButton.setSelected(true);
                    Traveler.addToWishlist(bookingItem, BookingItemDetailsFragment.this);
                }
            }
        });
        return view;
    }

    @Override
    public void onWishlistAddSuccess(Product item, CatalogItemDetails itemDetails) {
        bookingItem = (BookingItem) item;
        bookingItemDetails = (BookingItemDetails) itemDetails;
        wishlistItemChangedCallback.onItemWishlistStateChanged(itemDetails);
    }

    @Override
    public void onWishlistAddError(Error error) {
        wishListToggleImageButton.setSelected(false);
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(error.getMessage())
                .setCancelable(false)
                .setNeutralButton("Ok", null)
                .create()
                .show();
    }

    @Override
    public void onWishlistRemoveSuccess(Product item, CatalogItemDetails itemDetails) {
        bookingItem = (BookingItem) item;
        bookingItemDetails = (BookingItemDetails) itemDetails;
        if (wishlistItemChangedCallback != null) {
            wishlistItemChangedCallback.onItemWishlistStateChanged(itemDetails);
        }
    }

    @Override
    public void onWishlistRemoveError(Error error, WishlistResult result) {
        wishListToggleImageButton.setSelected(true);
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(error.getMessage())
                .setCancelable(false)
                .setNeutralButton("Ok", null)
                .create()
                .show();
    }
}
