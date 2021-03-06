package com.project.boostcamp.staffdinnerrestraurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.data.AdminEstimate;
import com.project.boostcamp.publiclibrary.data.ViewType;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder.EstimateVH;
import com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder.LoadingVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 */

public class EstimateAdapter extends RecyclerView.Adapter<BaseVH> {
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

    public ArrayList<AdminEstimate> getData() {
        return data;
    }

    @Override
    public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ViewType.NORMAL) {
            return new EstimateVH(LayoutInflater.from(context).inflate(R.layout.item_estimate, parent, false), dataEvent, context);
        } else if(viewType == ViewType.LOADING) {
            return new LoadingVH(LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false), null);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseVH holder, int position) {
        if(holder instanceof EstimateVH) {
            ((EstimateVH)holder).setupView(data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) == null
                ? ViewType.LOADING
                : ViewType.NORMAL;
    }
}
