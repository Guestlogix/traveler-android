package com.guestlogix.traveleruikit.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.travelercorekit.models.ContactInfo;
import com.guestlogix.travelercorekit.models.Location;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.CatalogItemInformationFragment;
import com.guestlogix.traveleruikit.fragments.CatalogItemProviderInformationFragment;

import java.util.List;

public class ItemInformationTabsPagerAdapter extends FragmentStatePagerAdapter {

    private ContactInfo contactInfo;
    private List<Attribute> informationList;
    private List<Location> locationsList;
    private Context context;

    public ItemInformationTabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setInformationList(List<Attribute> informationList) {
        this.informationList = informationList;
    }

    public void setLocationsList(List<Location> locationsList) {
        this.locationsList = locationsList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CatalogItemInformationFragment.getInstance(informationList);
            case 1:
                return CatalogItemProviderInformationFragment.getInstance(contactInfo, locationsList);
        }
        return CatalogItemInformationFragment.getInstance(informationList);
    }

    @Override
    public int getCount() {
        if (null != informationList && informationList.size() > 0) {
            return 2;
        }
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.info);
            case 1:
                return context.getString(R.string.provider);
            default:
                return null;
        }
    }
}
