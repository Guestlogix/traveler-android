package com.guestlogix.traveler.network;

import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.guestlogix.traveler.callbacks.ProfileFetchCallback;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.travelercorekit.UrlRequest;
import com.guestlogix.travelercorekit.tasks.BlockTask;
import com.guestlogix.travelercorekit.tasks.RemoteNetworkRequestTask;
import com.guestlogix.travelercorekit.utilities.TaskManager;

public class Guest {
    private static Guest localInstance = new Guest();
    private TaskManager taskManager;

    private Guest() {
        this.taskManager = new TaskManager();
    }

    /**
     * Fetches user profile against the google auth token.
     * <p>
     * @param account         google account of user.
     * @param profileCallback Callback methods which will be executed after the user is fetched.
     * @param applicationContext application context where the sdk is running.
     */
    public static void fetchProfile(GoogleSignInAccount account, ProfileFetchCallback profileCallback, Context applicationContext) {

        UrlRequest request = GuestRoute.profile(account.getIdToken(), applicationContext);

        RemoteNetworkRequestTask<Profile> fetchProfileTask = new RemoteNetworkRequestTask<>(request, new Profile.ProfileObjectMappingFactory());

        BlockTask fetchProfileBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != fetchProfileTask.getError()) {
                    profileCallback.onProfileFetchError(fetchProfileTask.getError());
                } else {
                    profileCallback.onProfileFetchSuccess(fetchProfileTask.getResource());
                }
            }
        };

        fetchProfileBlockTask.addDependency(fetchProfileTask);

        localInstance.taskManager.addTask(fetchProfileTask);
        TaskManager.getMainTaskManager().addTask(fetchProfileBlockTask);
    }
}
