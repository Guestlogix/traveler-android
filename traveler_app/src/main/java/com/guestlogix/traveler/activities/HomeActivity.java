package com.guestlogix.traveler.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.callbacks.ProfileFetchCallback;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.Traveler;


public class HomeActivity extends AppCompatActivity implements ProfileFetchCallback {
    final static int REQUEST_CODE_ADD_FLIGHT = 0;
    final static int REQUEST_CODE_SIGN_IN = 1;
    final static int REQUEST_CODE_PROFILE = 2;

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.profile = Profile.storedProfile(this);

        if (profile != null) {
            Traveler.identify(profile.getTravelerId());
        }

        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flight_add_action:
                Intent addFlightIntent = new Intent(HomeActivity.this, AddFlightActivity.class);
                startActivityForResult(addFlightIntent, REQUEST_CODE_ADD_FLIGHT);
                return true;
            case R.id.profile_action:
                Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                profileIntent.putExtra(ProfileActivity.ARG_PROFILE, profile);
                startActivityForResult(profileIntent, REQUEST_CODE_PROFILE);
                return true;
            case R.id.sign_in_action:
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
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_ADD_FLIGHT:
                //Flight flight = (Flight) data.getExtras().get(EXTRA_FLIGHT);
                //this.flights.add(flight);
                // update ui
                break;
            case REQUEST_CODE_SIGN_IN:
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult();
                if (account != null) {
                    Guest.fetchProfile(account, this);
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("Error signing in with Google")
                            .setCancelable(false)
                            .setPositiveButton("Ok ", null)
                            .create()
                            .show();
                }
                break;
            case REQUEST_CODE_PROFILE:
                GoogleSignInClient client = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
                client.signOut();
                profile = null;
                Profile.clearStoredProfile(this);
                Traveler.identify(null);
                invalidateOptionsMenu();
        }
    }

    @Override
    public void onProfileFetchSuccess(Profile profile) {
        this.profile = profile;
        profile.save(this);
        Traveler.identify(profile.getTravelerId());
        invalidateOptionsMenu();
    }

    @Override
    public void onProfileFetchError(Error error) {
        new AlertDialog.Builder(this)
                .setMessage("Error fetching profile")
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }
}

