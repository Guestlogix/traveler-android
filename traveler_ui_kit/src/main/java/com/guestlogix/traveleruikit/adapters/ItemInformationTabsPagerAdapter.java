package com.guestlogix.traveleruikit.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.guestlogix.travelercorekit.models.Attribute;
import com.guestlogix.travelercorekit.models.ContactInfo;
import com.guestlogix.travelercorekit.models.Location;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.ItemDetailInformationFragment;
import com.guestlogix.traveleruikit.fragments.ProviderInformationFragment;

import java.util.ArrayList;

public class ItemInformationTabsPagerAdapter extends FragmentPagerAdapter {

    private ContactInfo contactInfo;
    private ArrayList<Attribute> informationList;
    private ArrayList<Location> locationsList;
    private Context context;

    public ItemInformationTabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setInformationList(ArrayList<Attribute> informationList) {
        this.informationList = informationList;
    }

    public void setLocationsList(ArrayList<Location> locationsList) {
        this.locationsList = locationsList;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProviderInformationFragment.getInstance(contactInfo, locationsList);
            case 1:
                return ItemDetailInformationFragment.getInstance(informationList);
        }
        return null;
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
                return context.getString(R.string.provider);
            case 1:
                return context.getString(R.string.info);
            default:
                return null;
        }
    }
}
