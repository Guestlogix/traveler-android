package com.guestlogix.traveleruikit.tools.image;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import com.guestlogix.travelercorekit.task.BlockTask;
import com.guestlogix.travelercorekit.task.DownloadImageTask;
import com.guestlogix.travelercorekit.task.TaskManager;

import java.net.URL;

/**
 * Loads image in provided imageView.
 * Require ImageCache to cache image. It will lookup cache before making network call and set it as imageView's bitmap.
 */
public class ImageLoader {

    private TaskManager mTaskManager = new TaskManager();
    private ImageCache imageCache;

    public ImageLoader(ImageCache imageCache) {
        this.imageCache = imageCache;
    }

    public void loadImage(URL url, ImageView imageView) {

        DownloadImageTask imageDownloadTask = new DownloadImageTask(new ImageRequest(url));

        BlockTask imageDownloadBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (imageDownloadTask.getError() == null) {
                    setImageBitmap(imageView, imageDownloadTask.getResource());
                    imageCache.put(url.toString(), imageDownloadTask.getResource());
                } else {
                    //TODO: Failed to load image handle by default image or retry.
                    Log.e("Traveler", String.format("Could not load image for url %s", url));
                }
            }
        };

        BlockTask cacheLookupTask = new BlockTask() {
            @Override
            public void main() {
                //if image found in cache cancel all subsequent tasks and load cached image in imageView otherwise let the party rock n roll
                Bitmap cachedBitmap = imageCache.get(url.toString());
                if (null != cachedBitmap) {
                    setImageBitmap(imageView, cachedBitmap);
                    imageDownloadTask.cancel();
                    imageDownloadBlockTask.cancel();
                }
            }
        };

        imageDownloadTask.addDependency(cacheLookupTask);
        imageDownloadBlockTask.addDependency(imageDownloadTask);

        TaskManager.getMainTaskManager().addTask(cacheLookupTask);
        mTaskManager.addTask(imageDownloadTask);
        TaskManager.getMainTaskManager().addTask(imageDownloadBlockTask);
    }

    private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        if (null != bitmap) {
            imageView.setImageBitmap(bitmap);
        } else {
            //TODO: imageView.setImageBitmap(/*some default image*/);
        }
    }

}
