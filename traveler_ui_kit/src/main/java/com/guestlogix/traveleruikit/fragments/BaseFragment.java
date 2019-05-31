package com.guestlogix.traveleruikit.fragments;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.guestlogix.travelercorekit.TravelerLog;

public class BaseFragment extends Fragment {
    private FragmentActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (FragmentActivity) context;
        } else {
            TravelerLog.e("Parent must be an Activity");
        }
    }

    public FragmentActivity getActivityContext() {
        return activity;
    }
}
