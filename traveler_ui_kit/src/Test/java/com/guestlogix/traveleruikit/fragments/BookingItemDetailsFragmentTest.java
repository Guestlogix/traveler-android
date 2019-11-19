package com.guestlogix.traveleruikit.fragments;

import android.app.Application;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static com.guestlogix.traveleruikit.fragments.BookingItemDetailsFragment.ARG_BOOKING_ITEM;
import static com.guestlogix.traveleruikit.fragments.BookingItemDetailsFragment.ARG_BOOKING_ITEM_DETAILS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@Config(application = BookingItemDetailsFragmentTest.TestApplication.class)
public class BookingItemDetailsFragmentTest {
    private FragmentScenario<BookingItemDetailsFragment> fragmentScenario;
    private static final String ERROR_MESSAGE = "error";

    @Mock Error error;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(error.getMessage()).thenReturn(ERROR_MESSAGE);
    }

    public void setUpFragment(boolean isWishlisted) {
        List<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute("Excludes", "Nothing of note."));
        attributeList.add(new Attribute("Includes", "Your rental(s); Adult bikes, child bikes, child trailers, and child seats are all available.; Free bike helmet - on request.; Free bike lock - on request.; Free NYC bike map - on request.; Bike baskets are available.; Customer can reschedule their booking for any reason at no additional cost."));

        List<Location> locationList = new ArrayList<>();
        Location mockLocation = mock(Location.class);
        when(mockLocation.getAddress()).thenReturn("New York, United States");
        when(mockLocation.getLatitude()).thenReturn(0d);
        when(mockLocation.getLatitude()).thenReturn(0d);
        locationList.add(mockLocation);

        ContactInfo mockContackInfo = mock(ContactInfo.class);
        when(mockContackInfo.getName()).thenReturn("Bike Rent NYC");

        Supplier mockSupplier = mock(Supplier.class);

        BookingItemDetails bookingItemDetails = mock(BookingItemDetails.class);
        when(bookingItemDetails.getDescription()).thenReturn("\"Renting a bike and independently exploring New York City and Brooklyn is a memorable, favorite activity and fun for all ages! Choose from top-quality bike rentals close to the Brooklyn Bridge. <br><br>Explore lower Manhattan and ride over the Brooklyn Bridge - one of the oldest bridges in America and a National Historical Landmark. Visit iconic Brooklyn Bridge Park, the Brooklyn Greenway, the waterfront, Jane’s Carousel, DUMBO, other popular parts of trendy Brooklyn - and even some attractions of Lower Manhattan with your amazing bike rental! Tandem bike (meant for two people) may be provided if available (although not guaranteed and are counted as two bikes). You may also rent a bike from this location and drop it off at another location (inquire at the shop for more info). Bike rentals are appropriate for all ages and skill levels, and one adult 18 or over must be present and responsible. Helmet(s), lock(s) and map(s) are free and available on request, helmets are mandatory for children under 14.<br><br>Adult bikes, child bikes, child trailers, and child seats are all available.<br><br>Bike baskets are available.<br><br>Customer can reschedule their booking for any reason at no additional cost.\"");
        when(bookingItemDetails.getContact()).thenReturn(mockContackInfo);
        when(bookingItemDetails.getInformation()).thenReturn(attributeList);
        when(bookingItemDetails.getLocations()).thenReturn(locationList);
        when(bookingItemDetails.getSupplier()).thenReturn(mockSupplier);
        when(bookingItemDetails.isWishlisted()).thenReturn(isWishlisted);

        BookingItem bookingItem = mock(BookingItem.class);
        when(bookingItem.getSubTitle()).thenReturn("\"Renting a bike and independently exploring New York City and Brooklyn is a memorable, favorite activity and fun for all ages! Choose from top-quality bike rentals close to the Brooklyn Bridge. <br><br>Explore lower Manhattan and ride over the Brooklyn Bridge - one of the oldest bridges in America and a National Historical Landmark. Visit iconic Brooklyn Bridge Park, the Brooklyn Greenway, the waterfront, Jane’s Carousel, DUMBO, other popular parts of trendy Brooklyn - and even some attractions of Lower Manhattan with your amazing bike rental! Tandem bike (meant for two people) may be provided if available (although not guaranteed and are counted as two bikes). You may also rent a bike from this location and drop it off at another location (inquire at the shop for more info). Bike rentals are appropriate for all ages and skill levels, and one adult 18 or over must be present and responsible. Helmet(s), lock(s) and map(s) are free and available on request, helmets are mandatory for children under 14.<br><br>Adult bikes, child bikes, child trailers, and child seats are all available.<br><br>Bike baskets are available.<br><br>Customer can reschedule their booking for any reason at no additional cost.\"");
        when(bookingItem.getTitle()).thenReturn("Bike Rent NYC");
        when(bookingItem.isWishlisted()).thenReturn(isWishlisted);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_BOOKING_ITEM_DETAILS, bookingItemDetails);
        bundle.putSerializable(ARG_BOOKING_ITEM, bookingItem);
        fragmentScenario = FragmentScenario.launch(BookingItemDetailsFragment.class, bundle);
    }

    @Test
    public void onWishlistAddError() {
        setUpFragment(true);

        fragmentScenario.onFragment(fragment -> {
            ImageButton toggleButton = fragment.getView().findViewById(R.id.imagebutton_wishlist_toggle);
            Assert.assertTrue(toggleButton.isSelected());

            fragment.onWishlistAddError(error);
            Assert.assertFalse(toggleButton.isSelected());
        });
    }

    @Test
    public void onWishlistRemoveError() {
        setUpFragment(false);

        fragmentScenario.onFragment(fragment -> {
                ImageButton toggleButton = fragment.getView().findViewById(R.id.imagebutton_wishlist_toggle);
            Assert.assertFalse(toggleButton.isSelected());

            fragment.onWishlistRemoveError(error, null);
            Assert.assertTrue(toggleButton.isSelected());
        });
    }

    public static class TestApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            setTheme(R.style.Theme_MaterialComponents); //or just R.style.Theme_AppCompat
        }
    }
}
