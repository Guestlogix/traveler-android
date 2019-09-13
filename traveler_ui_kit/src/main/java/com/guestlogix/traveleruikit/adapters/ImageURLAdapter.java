package com.guestlogix.traveleruikit.adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;

import java.net.URL;
import java.util.List;

public class ImageURLAdapter extends RecyclerView.Adapter<ImageURLAdapter.ViewHolder> {
    private List<URL> data;

    public ImageURLAdapter(List<URL> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_carousel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Resources resources = holder.imageView.getResources();
        AssetManager.getInstance().loadImage(data.get(position),
                (int) resources.getDimension(R.dimen.thumbnail_width),
                (int) resources.getDimension(R.dimen.thumbnail_height),
                holder.imageView.getId(),
                new ImageLoader.ImageLoaderCallback() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap) {
                        holder.imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError() {
                        holder.imageView.setImageResource(R.color.colorPrimary);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return (null != data) ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
