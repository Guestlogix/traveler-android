package com.guestlogix.traveler.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.traveler.R;
import com.guestlogix.traveleruikit.fragments.BaseFragment;

/**
 * A fragment used to display content with minimal controls.
 * Expects the layoutId and the title in safe args.
 */
public class AppInformationFragment extends BaseFragment {

    @LayoutRes
    private int layoutId;
    private String title;

    public AppInformationFragment() {
        // Do nothing.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppInformationFragmentArgs args = AppInformationFragmentArgs.fromBundle(getArguments());
        layoutId = args.getLayoutId();
        title = args.getTitle();
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            ActionBar actionBar = activity.getSupportActionBar();

            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(null);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(title);
            }
        }

        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavController navController = Navigation.findNavController(getActivityContext(), R.id.nav_app_settings);
            navController.popBackStack();
        }

        return true;
    }

}
