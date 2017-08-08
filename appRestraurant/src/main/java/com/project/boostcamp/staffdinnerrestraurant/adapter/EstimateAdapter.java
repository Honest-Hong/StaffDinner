package com.project.boostcamp.staffdinnerrestraurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.data.AdminEstimate;
import com.project.boostcamp.publiclibrary.data.DataEvent;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder.EstimateVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 */

public class EstimateAdapter extends RecyclerView.Adapter<EstimateVH> {
    private Context context;
    private ArrayList<AdminEstimate> data;
    private DataEvent<AdminEstimate> dataEvent;

    public EstimateAdapter(Context context, DataEvent<AdminEstimate> dataEvent) {
        data = new ArrayList<>();
        this.dataEvent = dataEvent;
        this.context = context;
    }

    public void setData(ArrayList<AdminEstimate> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public EstimateVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EstimateVH(LayoutInflater.from(context).inflate(R.layout.layout_estimate_item, parent, false), dataEvent, context);
    }

    @Override
    public void onBindViewHolder(EstimateVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
