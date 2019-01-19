package com.guestlogix.travelercorekit.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.network.Router;

import java.net.URL;


public class DownloadImageTask extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private URL imageURL;
    private TravelerError mError;
    private Bitmap resource;


    public DownloadImageTask(URL imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public void execute() {

        Log.v("Traveler", "DownloadImageTask execute()");

        //Fetch image from backend
        NetworkTask fetchTokenNetworkTask = new NetworkTask(Router.downloadImage(imageURL), stream -> resource = BitmapFactory.decodeStream(stream));

        BlockTask finishTask = new BlockTask() {
            @Override
            protected void main() {
                DownloadImageTask.this.finish();
            }
        };

        finishTask.addDependency(fetchTokenNetworkTask);

        mTaskManager.addTask(fetchTokenNetworkTask);
        mTaskManager.addTask(finishTask);
    }

    public TravelerError getError() {
        return mError;
    }

    public Bitmap getResource() {
        return resource;
    }
}
