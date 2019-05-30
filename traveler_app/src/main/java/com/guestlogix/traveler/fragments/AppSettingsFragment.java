package com.guestlogix.traveler.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.activities.HomeActivity;
import com.guestlogix.traveler.network.Guest;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for the navigation of all the fragments
 */
public class AppSettingsFragment extends BaseFragment implements View.OnClickListener {

    private List<String> actions = new ArrayList<>();

    private String version;

    public AppSettingsFragment() {
        // Do nothing.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            PackageInfo info = getContext().getPackageManager().getPackageInfo(getActivityContext().getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            TravelerLog.e("Could not fetch version number.");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_app_settings, container, false);

        if (actions.isEmpty()) {
            actions.add(getString(R.string.support));
            actions.add(String.format(getString(R.string.app_version), version));
            actions.add(getString(R.string.faq));
            actions.add(getString(R.string.legal));
            actions.add(getString(R.string.privacy_policy));
            actions.add(getString(R.string.sign_out));
        }

        RecyclerView actionsRV = v.findViewById(R.id.recyclerView_appSettings_actionsList);
        SettingAdapter adapter = new SettingAdapter();
        actionsRV.setAdapter(adapter);

        LinearLayoutManager lm = new LinearLayoutManager(v.getContext());
        actionsRV.setLayoutManager(lm);
        actionsRV.addItemDecoration(new DividerItemDecoration(v.getContext(), lm.getOrientation()));

        ImageButton imageButton = v.findViewById(R.id.imageButton_appSettings_back);
        imageButton.setOnClickListener(_v ->
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
                // App version do nothing.
                break;
            case 2:
                layoutId = R.layout.fragment_frequently_asked_questions;
                title = getString(R.string.faq);
                action = AppSettingsFragmentDirections.actionToInfoDest(layoutId, title);

                nav.navigate(action);
                break;
            case 3:
                layoutId = R.layout.fragment_legal_information;
                title = getString(R.string.legal);
                action = AppSettingsFragmentDirections.actionToInfoDest(layoutId, title);

                nav.navigate(action);
                break;
            case 4:
                layoutId = R.layout.fragment_privacy_policy;
                title = getString(R.string.privacy_policy);
                action = AppSettingsFragmentDirections.actionToInfoDest(layoutId, title);

                nav.navigate(action);
                break;
            case 5:
                final Dialog d = new Dialog(getActivityContext());

                d.setContentView(R.layout.dialog_alert);
                TextView dTitle = d.findViewById(R.id.textView_alertDialog_title);
                TextView msg = d.findViewById(R.id.textView_alertDialog_message);
                Button cancel = d.findViewById(R.id.button_alertDialog_negativeButton);
                Button confirm = d.findViewById(R.id.button_alertDialog_positiveButton);


                dTitle.setText(R.string.confirm_sign_out);
                msg.setText(R.string.sign_out_msg);

                confirm.setText(R.string.confirm);
                confirm.setOnClickListener(b -> onSignOut(d));

                cancel.setText(R.string.cancel);
                cancel.setOnClickListener(b -> d.dismiss());

                d.show();
                break;
        }
    }

    private void onSignOut(DialogInterface d) {
        d.dismiss();

        String clientId = BuildConfig.GOOGL_SIGN_IN_CLIENT_ID;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build();

        GoogleSignInClient gSignInClient = GoogleSignIn.getClient(getActivityContext(), gso);

        gSignInClient.signOut().addOnCompleteListener(getActivityContext(), task -> {

            Guest.getInstance().logout(getActivityContext());
            Traveler.removeIdentity();

            Intent i = new Intent(getActivity(), HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
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
