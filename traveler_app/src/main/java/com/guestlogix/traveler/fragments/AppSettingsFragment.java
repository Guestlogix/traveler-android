package com.guestlogix.traveler.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveleruikit.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for the navigation of all the fragments
 */
public class AppSettingsFragment extends BaseFragment implements View.OnClickListener {
    private List<String> actions = new ArrayList<>();

    public AppSettingsFragment() {
        // Do nothing.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_settings_fragment, container, false);

        if (actions.isEmpty()) {
            // Items
            actions.add(getString(R.string.support));
            actions.add(getString(R.string.faq));
            actions.add(getString(R.string.legal));
            actions.add(getString(R.string.privacy_policy));
        }

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView_appSettings_actionsList);
        recyclerView.setAdapter(new SettingAdapter());

        LinearLayoutManager lm = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), lm.getOrientation()));

        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            ActionBar actionBar = activity.getSupportActionBar();

            if (actionBar != null) {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                        ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);

                actionBar.setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
            }
        }

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
//                nav.navigate(R.id.action_home_destination_to_privacy_policy_destination);
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavController navController = Navigation.findNavController(getActivityContext(), R.id.nav_app_settings);
            navController.popBackStack();
        }

        return true;
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
