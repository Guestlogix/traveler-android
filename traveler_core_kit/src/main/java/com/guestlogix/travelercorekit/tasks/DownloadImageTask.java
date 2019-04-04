package com.guestlogix.travelercorekit.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.TravelerErrorCode;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.Task;
import com.guestlogix.travelercorekit.utilities.TaskManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DownloadImageTask extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private NetworkTask.Request imageRequest;
    private Error mError;
    private Bitmap resource;
    private int height;
    private int width;


    public DownloadImageTask(NetworkTask.Request imageRequest, int reqWidth, int reqHeight) {
        this.imageRequest = imageRequest;
        this.height = reqHeight;
        this.width = reqWidth;
    }

    @Override
    public void execute() {

        NetworkTask downloadImageNetworkTask = new NetworkTask(imageRequest, responseHandler);

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

    NetworkTask.ResponseHandler responseHandler = new NetworkTask.ResponseHandler() {
        @Override
        public void onHandleResponse(InputStream stream) {
            resource = decodeImageStream(stream);
        }

        @Override
        public void onHandleError(InputStream stream) {
            try (JsonReader reader = new JsonReader(new InputStreamReader(stream))) {
                mError = new NetworkTaskError.NetworkTaskErrorMappingFactory().instantiate(reader);
            } catch (IOException | IllegalStateException e) {
                mError = new TravelerError(TravelerErrorCode.PARSING_ERROR, String.format("Invalid JSON stream: %s", e.getMessage()));
            } catch (ObjectMappingException e) {
                mError = new TravelerError(TravelerErrorCode.PARSING_ERROR, String.format("Invalid JSON stream: %s", e.getMessage()));
            }
        }
    };

    private Bitmap decodeImageStream(InputStream stream) {

        BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            if (bufferedInputStream.available() > 0) {
                bufferedInputStream.mark(bufferedInputStream.available());
                BitmapFactory.decodeStream(bufferedInputStream, null, options);

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, width, height);
                bufferedInputStream.reset();
            }

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(bufferedInputStream, null, options);

        } catch (IOException e) {
            TravelerLog.e("Could not decode stream to bitmap %s", e.getMessage());
            return null;
        }
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Error getError() {
        return mError;
    }

    public Bitmap getResource() {
        return resource;
    }
}