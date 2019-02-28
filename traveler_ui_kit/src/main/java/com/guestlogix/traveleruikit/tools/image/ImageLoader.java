package com.guestlogix.traveleruikit.tools.image;

import android.graphics.Bitmap;
import com.guestlogix.travelercorekit.task.DownloadImageTask;
import com.guestlogix.travelercorekit.tasks.BlockTask;
import com.guestlogix.travelercorekit.tasks.TaskManager;
import com.guestlogix.travelercorekit.utilities.TravelerLog;

import java.net.URL;

/**
 * Loads image in provided imageView.
 * Requires ImageCache to cache images. It will lookup cache before making network call.
 */
public class ImageLoader {

    private TaskManager mTaskManager = new TaskManager();
    private ImageCache imageCache;

    public ImageLoader(ImageCache imageCache) {
        this.imageCache = imageCache;
    }

    /**
     *
     * @param url absolute url to the image to download.
     * @param imageLoaderCallback to get callback with the downloaded image bitmap
     */
    public void loadImage(URL url, ImageLoaderCallback imageLoaderCallback) {

        //if image found in cache cancel all subsequent tasks and load cached image in imageView otherwise let the party rock n roll
        Bitmap cachedBitmap = imageCache.get(url.toString());

        if (null != cachedBitmap) {
            imageLoaderCallback.onBitmapLoaded(cachedBitmap);
        } else {
            DownloadImageTask imageDownloadTask = new DownloadImageTask(new ImageRequest(url));

            BlockTask imageDownloadBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (imageDownloadTask.getError() == null) {
                        Bitmap loadedBitmap =  imageDownloadTask.getResource();
                        imageLoaderCallback.onBitmapLoaded(loadedBitmap);
                        imageCache.put(url.toString(), loadedBitmap);
                    } else {
                        //TODO: Failed to load image handle by default image or retry.
                        TravelerLog.e(String.format("Could not load image for url %s", url));
                    }
                }
            };

            imageDownloadBlockTask.addDependency(imageDownloadTask);

            mTaskManager.addTask(imageDownloadTask);
            TaskManager.getMainTaskManager().addTask(imageDownloadBlockTask);
        }
    }

    public interface ImageLoaderCallback {
        void onBitmapLoaded(Bitmap bitmap);
    }
}
