package com.project.boostcamp.staffdinnerrestraurant.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder.ImageVH;

/**
 * Created by Hong Tae Joon on 2017-08-16.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageVH> {
    private Context context;
    private ArraySet<String> data;
    private DataEvent<String> dataEvent;

    public ImageRecyclerAdapter(Context context, DataEvent<String> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
        this.data = new ArraySet<>();
    }

    public ArraySet<String> getData() {
        return data;
    }

    public void setData(ArraySet<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageVH(LayoutInflater.from(context).inflate(R.layout.item_image_list, parent, false), dataEvent);
    }

    @Override
    public void onBindViewHolder(ImageVH holder, int position) {
        holder.setupView(data.valueAt(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
