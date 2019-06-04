package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    private Profile user;
    private NavController nav;
    private LinearLayout viewOrders;

    public ProfileFragment() {
        // Do nothing.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        user = Guest.getInstance().getUserProfile(getActivityContext());
        nav = Navigation.findNavController(getActivityContext(), R.id.nav_app_settings);

        CollapsingToolbarLayout layout = v.findViewById(R.id.collapsingToolbar_profile_title);

        if (null != user) {
            layout.setTitle(String.format("%s %s", user.getFirstName(), user.getLastName()));
            RecyclerView rv = v.findViewById(R.id.recyclerView_profile_userInfo);
            rv.setAdapter(new ProfileInformationAdapter());
            rv.setLayoutManager(new LinearLayoutManager(getActivityContext()));

            viewOrders = v.findViewById(R.id.linearLayout_profile_orders);
            viewOrders.setOnClickListener(this::onOrdersClick);
        }

        FloatingActionButton fab = v.findViewById(R.id.fab_profile_editBtn);
        fab.setOnClickListener(this::onEditProfileClick);

        ImageButton settings = v.findViewById(R.id.imageButton_profile_settings);
        settings.setOnClickListener(this::onSettingsClick);

        ImageButton back = v.findViewById(R.id.imageButton_profile_back);
        back.setOnClickListener(this::onBackClick);

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

    private void onBackClick(View _back) {
        getActivityContext().finish();
    }

    private void onOrdersClick(View _v) {
    }

    class ProfileInformationAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_profile_information, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.icon.setBackgroundResource(R.drawable.ic_email_24dp);
            holder.key.setText(R.string.email);
            holder.value.setText(user.getEmail());
        }

        @Override
        public int getItemCount() {
            return 1; // TODO: Change when we can display credit card info.
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView key;
        TextView value;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.imageView_profileItem_icon);
            key = itemView.findViewById(R.id.textView_profileItem_title);
            value = itemView.findViewById(R.id.textView_profileItem_value);
        }
    }
}
