package com.project.boostcamp.staffdinner.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.data.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ClientEstimateDTO;
import com.project.boostcamp.staffdinner.R;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-04.
 */

public class EstimateRecyclerAdapter extends RecyclerView.Adapter<EstimateVH> {
    private Context context;
    private ArrayList<ClientEstimateDTO> data;
    private DataEvent<ClientEstimateDTO> dataEvent;

    public EstimateRecyclerAdapter(Context context, DataEvent<ClientEstimateDTO> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
        data = new ArrayList<>();
    }

    public void setData(ArrayList<ClientEstimateDTO> data) {
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
