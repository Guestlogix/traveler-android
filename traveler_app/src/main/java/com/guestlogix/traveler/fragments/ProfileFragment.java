package com.guestlogix.traveler.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;
import com.guestlogix.traveleruikit.fragments.BaseFragment;

/**
 * A fragment which displays the current profile.
 */
public class ProfileFragment extends BaseFragment {
    Profile user;
    NavController nav;


    public ProfileFragment() {
        // Do nothing.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        user = Guest.getInstance().getSignedInUser(getActivityContext());
        if (null == user) {
            getActivityContext().finish();
            return null;
        }

        nav = Navigation.findNavController(getActivityContext(), R.id.nav_app_settings);

        CollapsingToolbarLayout layout = v.findViewById(R.id.collapsingToolbar_profile_title);
        layout.setTitle(String.format("%s %s", user.getFirstName(), user.getLastName()));

        FloatingActionButton fab = v.findViewById(R.id.fab_profile_editBtn);
        fab.setOnClickListener(this::onEditProfileClick);

        ImageButton settings = v.findViewById(R.id.imageButton_profile_settings);
        settings.setOnClickListener(this::onSettingsClick);

        return v;
    }

    private void onEditProfileClick(View fab) {
        NavDirections action = ProfileFragmentDirections.actionProfileDestinationToEditProfileDestination();
        nav.navigate(action);
    }

    private void onSettingsClick(View _settings) {
        NavDirections action = ProfileFragmentDirections.actionProfileDestToHomeDestination();
        nav.navigate(action);

    }

    private void onOrdersClick(View _v) {
        // TODO: When we have a see all orders screen.
    }
}
