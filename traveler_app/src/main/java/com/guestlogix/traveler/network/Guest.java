package com.guestlogix.traveler.network;

import android.content.Context;
import com.guestlogix.traveler.callbacks.ProfileCallback;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.travelercorekit.UrlRequest;
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
     * @param authToken      google authToken of user.
     * @param profileCallback Callback methods which will be executed after the user is fetched.
     */
    public void fetchProfile(String authToken, ProfileCallback profileCallback) {

        UrlRequest request = GuestRoute.fetchProfile(authToken);

        RemoteNetworkRequestTask<Profile> fetchProfileTask = new RemoteNetworkRequestTask<>(request, new Profile.ProfileObjectMappingFactory());

        BlockTask fetchProfileBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != fetchProfileTask.getError()) {
                    profileCallback.onSignInError(fetchProfileTask.getError());
                } else {
                    profileCallback.onSignInSuccess(fetchProfileTask.getResource());
                }
            }
        };

        fetchProfileBlockTask.addDependency(fetchProfileTask);

        localInstance.taskManager.addTask(fetchProfileTask);
        TaskManager.getMainTaskManager().addTask(fetchProfileBlockTask);
    }

    /**
     * Removes signed in user from shared prefs.
     *
     * @param context calling context
     */
    public void logout(Context context) {
        Profile.remove(context);
    }

    /**
     * Get the last signed in user if saved in shared prefs null otherwise.
     *
     * @param context calling context
     */
    public Profile getSignedInUser(Context context) {
        return Profile.read(context);
    }
}
