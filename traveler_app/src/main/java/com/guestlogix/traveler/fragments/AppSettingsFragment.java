package com.guestlogix.traveler.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.traveleruikit.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import static com.guestlogix.traveler.viewmodels.HomeViewModel.ADD_FLIGHT_REQUEST_CODE;
import static com.guestlogix.traveler.viewmodels.HomeViewModel.EXTRA_FLIGHT;
import static com.guestlogix.traveler.viewmodels.HomeViewModel.REQUEST_CODE_SIGN_IN;

/**
 * Container for the navigation of all the fragments
 */
public class AppSettingsFragment extends BaseFragment implements View.OnClickListener {
    private List<String> actions = new ArrayList<>();
    private Profile user;

    private SettingAdapter adapter;

    public AppSettingsFragment() {
        // Do nothing.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_app_settings, container, false);
        user = Guest.getInstance().getSignedInUser(getActivityContext());

        if (actions.isEmpty()) {
            // Items
            actions.add(getString(R.string.support));
            actions.add(getString(R.string.faq));
            actions.add(getString(R.string.legal));
            actions.add(getString(R.string.privacy_policy));

            if (user != null) {
                actions.add(getString(R.string.sign_out));
            } else {
                actions.add(getString(R.string.sign_in));
            }
        }

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView_appSettings_actionsList);
        adapter = new SettingAdapter();
        recyclerView.setAdapter(adapter);

        LinearLayoutManager lm = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), lm.getOrientation()));

        ImageButton imageButton = v.findViewById(R.id.imageButton_appSettings_back);
        imageButton.setOnClickListener(_button ->
                Navigation.findNavController(getActivityContext(), R.id.nav_app_settings).popBackStack());

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        NavController nav = Navigation.findNavController(getActivity(), R.id.nav_app_settings);

        String title;
        int layoutId;
        AppSettingsFragmentDirections.ActionToInfoDest action;

        switch (position) {
            case 0:
                layoutId = R.layout.fragment_support_information;
                title = getString(R.string.support);
                action = AppSettingsFragmentDirections.actionToInfoDest(layoutId, title);

                nav.navigate(action);
                break;
            case 1:
                layoutId = R.layout.fragment_frequently_asked_questions;
                title = getString(R.string.faq);
                action = AppSettingsFragmentDirections.actionToInfoDest(layoutId, title);

                nav.navigate(action);
                break;
            case 2:
                layoutId = R.layout.fragment_legal_information;
                title = getString(R.string.legal);
                action = AppSettingsFragmentDirections.actionToInfoDest(layoutId, title);

                nav.navigate(action);
                break;
            case 3:
                layoutId = R.layout.fragment_privacy_policy;
                title = getString(R.string.privacy_policy);
                action = AppSettingsFragmentDirections.actionToInfoDest(layoutId, title);

                nav.navigate(action);
                break;
            case 4:
                if (user != null) {
                    Guest.getInstance().logout(getActivityContext());
                } else {
                    // TODO: Log in.
                }
                adapter.notifyItemChanged(4);
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    class SettingAdapter extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_action_settings, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setText(actions.get(position));
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(AppSettingsFragment.this);
        }

        @Override
        public int getItemCount() {
            return actions.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setClickable(true);
            title = itemView.findViewById(R.id.textView_appSettingsItem_title);
        }
    }
}
