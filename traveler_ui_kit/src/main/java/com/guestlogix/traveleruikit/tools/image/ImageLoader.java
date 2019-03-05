package com.guestlogix.traveleruikit.tools.image;

import android.graphics.Bitmap;
import com.guestlogix.travelercorekit.network.UrlRequest;
import com.guestlogix.travelercorekit.tasks.DownloadImageTask;
import com.guestlogix.travelercorekit.tasks.BlockTask;
import com.guestlogix.travelercorekit.tasks.NetworkTask;
import com.guestlogix.travelercorekit.tasks.Task;
import com.guestlogix.travelercorekit.tasks.TaskManager;

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
     * @param url                 absolute url to the image to download.
     * @param height              requested height of image
     * @param width               requested width of image
     * @param imageLoaderCallback to get callback with the downloaded image bitmap.
     */
    public Task loadImage(URL url, int width, int height, ImageLoaderCallback imageLoaderCallback) {

        //if image found in cache notify observer with bitmap, otherwise start image download
        Bitmap cachedBitmap = imageCache.get(url.toString());

        if (null != cachedBitmap) {
            imageLoaderCallback.onBitmapLoaded(cachedBitmap);
        } else {
            final Bitmap[] loadedBitmap = new Bitmap[1];

            DownloadImageTask imageDownloadTask = new DownloadImageTask(new UrlRequest(NetworkTask.Request.Method.GET, url), width, height);
            BlockTask cacheImageTask = new BlockTask() {
                @Override
                protected void main() {
                    imageCache.put(url.toString(), loadedBitmap[0]);
                }
            };

            BlockTask imageDownloadBlockTask = new BlockTask() {
                @Override
                protected void main() {
                    if (imageDownloadTask.getError() == null && null != imageDownloadTask.getResource()) {
                        loadedBitmap[0] = imageDownloadTask.getResource();
                        imageLoaderCallback.onBitmapLoaded(loadedBitmap[0]);
                    } else {
                        imageLoaderCallback.onError();
                        cacheImageTask.cancel();
                    }
                }
            };

            imageDownloadBlockTask.addDependency(imageDownloadTask);
            cacheImageTask.addDependency(imageDownloadBlockTask);

            mTaskManager.addTask(imageDownloadTask);
            TaskManager.getMainTaskManager().addTask(imageDownloadBlockTask);
            mTaskManager.addTask(cacheImageTask);

        }
        return null;
    }

    public interface ImageLoaderCallback {
        void onBitmapLoaded(Bitmap bitmap);

        void onError();
    }
}
