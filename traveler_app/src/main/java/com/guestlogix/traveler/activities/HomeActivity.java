package com.guestlogix.traveler.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.FlightsSummaryAdapter;
import com.guestlogix.traveler.callbacks.ProfileFetchCallback;
import com.guestlogix.traveleruikit.fragments.ProgressDialogFragment;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.fragments.CatalogFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements ProfileFetchCallback, FlightsSummaryAdapter.Listener {
    final static int REQUEST_CODE_ADD_FLIGHT = 0;
    final static int REQUEST_CODE_SIGN_IN = 1;
    final static int REQUEST_CODE_PROFILE = 2;

    private Profile profile;
    private ArrayList<Flight> flights = new ArrayList<>();
    private RecyclerView flightsRecyclerView;
    private FragmentTransactionQueue transactionQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.profile = Profile.storedProfile(this);

        if (profile != null) {
            Traveler.identify(profile.getTravelerId());
        }

        transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        setContentView(R.layout.activity_home);

        flightsRecyclerView = findViewById(R.id.recyclerView_home_flights);
        flightsRecyclerView.setAdapter(new FlightsSummaryAdapter(flights, this));

        reloadCatalog();
    }

    private void reloadCatalog() {
        CatalogQuery query = new CatalogQuery(flights);
        CatalogFragment fragment = CatalogFragment.newInstance(query);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.catalog_container_layout, fragment);

        transactionQueue.addTransaction(transaction);
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
                Intent addFlightIntent = new Intent(this, FlightSearchActivity.class);
                startActivityForResult(addFlightIntent, REQUEST_CODE_ADD_FLIGHT);
                return true;
            case R.id.profile_action:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
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
            case R.id.settings_action:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        transactionQueue.setSuspended(false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        transactionQueue.setSuspended(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_ADD_FLIGHT:
                Flight flight = (Flight) data.getExtras().get(FlightSearchActivity.EXTRA_FLIGHT);
                this.flights.add(flight);
                flightsRecyclerView.getAdapter().notifyDataSetChanged();
                reloadCatalog();
                break;
            case REQUEST_CODE_SIGN_IN:
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult();
                if (account != null) {
                    ProgressDialogFragment fragment = new ProgressDialogFragment();
                    transactionQueue.addTransaction(fragment.getTransaction(getSupportFragmentManager()));

                    Guest.fetchProfile(account, this, getApplicationContext());
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
                reloadCatalog();
        }
    }

    @Override
    public void onProfileFetchSuccess(Profile profile) {
        this.profile = profile;
        profile.save(this);
        Traveler.identify(profile.getTravelerId());
        removeProgressDialogFragment();
        invalidateOptionsMenu();
        reloadCatalog();
    }

    @Override
    public void onProfileFetchError(Error error) {
        removeProgressDialogFragment();

        // TODO: Use fragments and fragment queue
        new AlertDialog.Builder(this)
                .setMessage("Error fetching profile")
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        if (ProgressDialogFragment.findExistingFragment(getSupportFragmentManager()) == null)
            super.onBackPressed();
    }

    private void removeProgressDialogFragment() {
        ProgressDialogFragment fragment = ProgressDialogFragment.findExistingFragment(getSupportFragmentManager());
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.remove(fragment);
        transactionQueue.addTransaction(transaction);
    }

    // FlightsSummaryAdapter.Listener

    @Override
    public void onRemoveFlight(int position) {
        flights.remove(position);
        flightsRecyclerView.getAdapter().notifyDataSetChanged();
        reloadCatalog();
    }

    @Override
    public void onClick(int position) {
        Flight flight = flights.get(position);
        Intent intent = new Intent(this, FlightActivity.class);
        intent.putExtra(FlightActivity.ARG_FLIGHT, flight);
        startActivity(intent);
    }
}

