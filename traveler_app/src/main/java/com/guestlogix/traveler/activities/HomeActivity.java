package com.guestlogix.traveler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.viewmodels.HomeViewModel;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.Traveler;

import static com.guestlogix.traveler.viewmodels.HomeViewModel.*;

public class HomeActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    private Menu menu;
    private Profile profile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeViewModel = ViewModelProviders.of(HomeActivity.this).get(HomeViewModel.class);
        homeViewModel.getObservableProfile().observe(this, this::handleProfile);
    }

    @Override
    protected void onStart() {
        super.onStart();
        homeViewModel.lookupProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        this.menu = menu;

        updateMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flight_add_action:
                Intent addFlightIntent = new Intent(HomeActivity.this, AddFlightActivity.class);
                startActivityForResult(addFlightIntent, ADD_FLIGHT_REQUEST_CODE);
                return true;
            case R.id.profile_action:
                Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            case R.id.sign_in_action:
                //TODO: using server client key to fetch requestIdToken
                // android app client id results in APIException if we request for RequestIdToken/AuthToken
                // https://stackoverflow.com/questions/52407793/com-google-android-gms-common-api-apiexception-16
                String clientId = BuildConfig.GOOGL_SIGN_IN_CLIENT_ID;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(clientId)
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && null != data) {
            switch (requestCode) {
                case ADD_FLIGHT_REQUEST_CODE:
                    Bundle extras = data.getExtras();
                    if (null != extras && extras.containsKey(EXTRA_FLIGHT)) {
                        Flight flight = (Flight) extras.getSerializable(EXTRA_FLIGHT);
                        homeViewModel.addFlight(flight);
                    }
                    break;
                case REQUEST_CODE_SIGN_IN:
                    Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                        if (account != null) {
                            homeViewModel.fetchProfile(account.getIdToken());
                        } else {
                            homeViewModel.setProfile(profile);
                        }
                    } catch (ApiException e) {
                        TravelerLog.e("HomeActivity", "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateMenuItems() {

        if (null == this.menu)
            return;

        MenuItem profileMenuItem = menu.findItem(R.id.profile_action);
        MenuItem signInMenuItem = menu.findItem(R.id.sign_in_action);
        if (null != profile) {
            profileMenuItem.setVisible(true);
            signInMenuItem.setVisible(false);
            profileMenuItem.setTitle(profile.getFirstName());
        } else {
            profileMenuItem.setVisible(false);
            signInMenuItem.setVisible(true);
        }
    }

    private void handleProfile(Profile _profile) {

        this.profile = _profile;
        if (this.profile != null) {
            Traveler.setUserId(_profile.getTravelerId());
        } else {
            Traveler.removeUserId();
        }
        updateMenuItems();
    }
}

