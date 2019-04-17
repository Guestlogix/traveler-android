package com.guestlogix.traveler.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
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
import com.guestlogix.traveler.activities.HomeActivity;
import com.guestlogix.traveler.callbacks.ProfileCallback;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.traveleruikit.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.guestlogix.traveler.viewmodels.HomeViewModel.REQUEST_CODE_SIGN_IN;

/**
 * Container for the navigation of all the fragments
 */
public class AppSettingsFragment extends BaseFragment implements View.OnClickListener, ProfileCallback {

    private List<String> actions = new ArrayList<>();
    private Profile user;
    RecyclerView actionsRV;

    private SettingAdapter adapter;
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
            // >:(
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_app_settings, container, false);
        user = Guest.getInstance().getSignedInUser(getActivityContext());

        if (actions.isEmpty()) {
            // Items
            actions.add(getString(R.string.support));
            actions.add(String.format(getString(R.string.app_version), version));
            actions.add(getString(R.string.faq));
            actions.add(getString(R.string.legal));
            actions.add(getString(R.string.privacy_policy));

            if (user != null) {
                actions.add(getString(R.string.delete_profile));
                actions.add(getString(R.string.sign_out));
            } else {
                actions.add(getString(R.string.sign_in));
            }
        }

        actionsRV = v.findViewById(R.id.recyclerView_appSettings_actionsList);
        adapter = new SettingAdapter();
        actionsRV.setAdapter(adapter);

        LinearLayoutManager lm = new LinearLayoutManager(v.getContext());
        actionsRV.setLayoutManager(lm);
        actionsRV.addItemDecoration(new DividerItemDecoration(v.getContext(), lm.getOrientation()));

        ImageButton imageButton = v.findViewById(R.id.imageButton_appSettings_back);
        imageButton.setOnClickListener(this::navigateBack);

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
                if (user != null) {
                    // TODO: Delete user.
                } else {
                    // Log In
                    String clientId = BuildConfig.GOOGL_SIGN_IN_CLIENT_ID;
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(clientId)
                            .requestEmail()
                            .build();

                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivityContext(), gso);
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
                }
                break;
            case 6:
                // Log Out
                Guest.getInstance().logout(getActivityContext());
                user = null;
                actions.remove(6); // Sign out
                actions.remove(5); // Delete profile
                adapter.notifyItemRangeRemoved(5, 2);
                actions.add(getString(R.string.sign_in));
                adapter.notifyItemInserted(5);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && null != data && requestCode == REQUEST_CODE_SIGN_IN) {
            try {
                Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                if (account != null) {
                    Guest.getInstance().fetchProfile(account.getIdToken(), this);
                }


            } catch (ApiException e) {
                TravelerLog.e("HomeActivity", "signInResult:failed code=" + e.getStatusCode());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSignInSuccess(Profile profile) {
        profile.save(getActivityContext());

        actions.remove(5);
        adapter.notifyItemRemoved(5);
        actions.add(getString(R.string.delete_profile));
        actions.add(getString(R.string.sign_out));
        adapter.notifyItemRangeInserted(5, 2);
    }

    @Override
    public void onSignInError(Error error) {
        Toast.makeText(getActivity(), "Could not sign in", Toast.LENGTH_SHORT).show();
    }

    private void navigateBack(View _v) {
        if (user != null) {
            Navigation.findNavController(getActivityContext(), R.id.nav_app_settings).popBackStack();
        } else {
            Intent i = new Intent(getActivity(), HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
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
