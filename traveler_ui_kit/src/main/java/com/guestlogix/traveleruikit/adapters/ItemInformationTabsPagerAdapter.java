package com.guestlogix.traveleruikit.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.ItemDetailInformationFragment;
import com.guestlogix.traveleruikit.fragments.ProviderInformationFragment;

public class ItemInformationTabsPagerAdapter extends FragmentPagerAdapter {
    private static final int TAB_COUNT = 2;

    public ItemInformationTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ItemDetailInformationFragment.getInstance();
            case 1:
                return ProviderInformationFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            case 0:
//                return getString(R.string.info);
//            case 1:
//                return getString(R.id.provider);
//            default:
//                return null;
//        }
//    }
}
