package com.project.boostcamp.staffdinnerrestraurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.data.AdminApplication;
import com.project.boostcamp.publiclibrary.data.DataEvent;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder.ApplicationVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 */

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationVH> {
    private Context context;
    private ArrayList<AdminApplication> data;
    private DataEvent<AdminApplication> dataEvent;

    public ApplicationAdapter(Context context, DataEvent<AdminApplication> dataEvent) {
        data = new ArrayList<>();
        this.dataEvent = dataEvent;
        this.context = context;
    }

    public void setData(ArrayList<AdminApplication> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ApplicationVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApplicationVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_apply_item, parent, false), dataEvent, context);
    }

    @Override
    public void onBindViewHolder(ApplicationVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
