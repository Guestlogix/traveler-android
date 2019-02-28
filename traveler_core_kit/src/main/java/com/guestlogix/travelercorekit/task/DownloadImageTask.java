package com.guestlogix.travelercorekit.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.tasks.BlockTask;
import com.guestlogix.travelercorekit.tasks.NetworkTask;
import com.guestlogix.travelercorekit.tasks.Task;
import com.guestlogix.travelercorekit.tasks.TaskManager;

public class DownloadImageTask extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private NetworkTask.Request imageRequest;
    private TravelerError mError;
    private Bitmap resource;


    public DownloadImageTask(NetworkTask.Request imageRequest) {
        this.imageRequest = imageRequest;
    }

    @Override
    public void execute() {

        //Fetch image from backend
        NetworkTask downloadImageNetworkTask = new NetworkTask(imageRequest, stream -> resource = BitmapFactory.decodeStream(stream));

        BlockTask finishTask = new BlockTask() {
            @Override
            protected void main() {
                mError = downloadImageNetworkTask.getError();
                DownloadImageTask.this.finish();
            }
        };

        finishTask.addDependency(downloadImageNetworkTask);

        mTaskManager.addTask(downloadImageNetworkTask);
        mTaskManager.addTask(finishTask);
    }

    public TravelerError getError() {
        return mError;
    }

    public Bitmap getResource() {
        return resource;
    }
}