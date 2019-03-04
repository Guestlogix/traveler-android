package com.guestlogix.traveleruikit.tools;

import android.util.SparseArray;
import com.guestlogix.travelercorekit.tasks.Task;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.tools.image.MemoryImageCache;

import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * This class manages assets for Traveler app.
 */
public class AssetManager {

    private static final AssetManager localInstance = new AssetManager();
    private ImageLoader imageLoader;
    private SparseArray<WeakReference<Task>> taskHashMap;

    private AssetManager() {
        MemoryImageCache imageCache = new MemoryImageCache();
        imageLoader = new ImageLoader(imageCache);
        taskHashMap = new SparseArray<>();
    }

    public static AssetManager getInstance() {
        return localInstance;
    }

    public void loadImage(URL url, int width, int height, int viewId, ImageLoader.ImageLoaderCallback imageLoaderCallback) {

        WeakReference<Task> weakTasks = taskHashMap.get(viewId);
        if (null != weakTasks) {
            Task previousTask = weakTasks.get();
            if (null != previousTask) {
                previousTask.cancel();
            }
        }

        Task imageLoadingTask = imageLoader.loadImage(url, width, height, imageLoaderCallback);
        taskHashMap.put(viewId, new WeakReference<>(imageLoadingTask));
    }
}

