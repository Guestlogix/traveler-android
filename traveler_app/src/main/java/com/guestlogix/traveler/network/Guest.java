package com.guestlogix.traveler.network;

import android.content.Context;
import com.guestlogix.traveler.callbacks.ProfileCallback;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.travelercorekit.UrlRequest;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.tasks.BlockTask;
import com.guestlogix.travelercorekit.tasks.RemoteNetworkRequestTask;
import com.guestlogix.travelercorekit.utilities.TaskManager;

public class Guest {

    private static Guest localInstance;
    private TaskManager taskManager;

    private Guest() {
        this.taskManager = new TaskManager();
    }

    public static Guest getInstance() {
        if (null == localInstance) {
            localInstance = new Guest();
        }
        return localInstance;
    }

    /**
     * Fetches user profile against the google auth token.
     * <p>
     *
     * @param authToken       google authToken of user.
     * @param profileCallback Callback methods which will be executed after the user is fetched.
     */
    public void fetchProfile(String authToken, ProfileCallback profileCallback) {

        UrlRequest request = GuestRoute.fetchProfile(authToken);

        RemoteNetworkRequestTask<Profile> fetchProfileTask = new RemoteNetworkRequestTask<>(request, new Profile.ProfileObjectMappingFactory());

        BlockTask fetchProfileBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != fetchProfileTask.getError()) {
                    profileCallback.onProfileError(fetchProfileTask.getError());
                } else {
                    profileCallback.onProfileReceived(fetchProfileTask.getResource());
                }
            }
        };

        fetchProfileBlockTask.addDependency(fetchProfileTask);

        localInstance.taskManager.addTask(fetchProfileTask);
        TaskManager.getMainTaskManager().addTask(fetchProfileBlockTask);
    }

    /**
     * Get the last signed in user profile, null if there is none.
     *
     * @param context calling context
     */
    public Profile getUserProfile(Context context) {
        return Profile.read(context);
    }

    /**
     * Saves profile in shared prefs and sets Traveler identity.
     *
     * @param context calling context
     * @param profile saves the profile in shared prefs. If null, profile will be removed from shared prefs
     */
    public void setUserProfile(Context context, Profile profile) {
        Profile.save(context, profile);

        if (null == profile) {
            Traveler.setIdentity(null);
        } else {
            Traveler.setIdentity(profile.getTravelerId());
        }
    }
}
