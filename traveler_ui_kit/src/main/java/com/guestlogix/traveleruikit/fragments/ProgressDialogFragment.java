package com.guestlogix.traveleruikit.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.guestlogix.traveleruikit.R;

import java.util.List;

public class ProgressDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public FragmentTransaction getTransaction(FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(this, "Progress");
        return transaction;
    }

    @Nullable
    static public ProgressDialogFragment findExistingFragment(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();

        for (Fragment fragment :
                fragments) {
            if (fragment instanceof ProgressDialogFragment)
                return (ProgressDialogFragment) fragment;
        }

        return null;
    }
}
