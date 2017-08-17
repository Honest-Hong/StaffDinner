package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ClientEstimateDTO;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.EmptyVH;
import com.project.boostcamp.staffdinner.adapter.viewholder.EstimateVH;

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
        if(this.data.size() == 0) {
            ClientEstimateDTO dto = new ClientEstimateDTO();
            dto.setViewType(-1);
            this.data.add(dto);
        }
        notifyDataSetChanged();
    }

    @Override
    public EstimateVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EstimateVH(LayoutInflater.from(context).inflate(R.layout.item_estimate, parent, false), dataEvent, context);
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
