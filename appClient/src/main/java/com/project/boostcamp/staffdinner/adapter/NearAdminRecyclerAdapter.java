package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.domain.NearAdminDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.NearAdminVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class NearAdminRecyclerAdapter extends RecyclerView.Adapter<NearAdminVH> {
    private Context context;
    private ArrayList<NearAdminDTO> data;
    private DataEvent<NearAdminDTO> dataEvent;

    public NearAdminRecyclerAdapter(Context context, DataEvent<NearAdminDTO> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
        this.data = new ArrayList<>();
    }

    public ArrayList<NearAdminDTO> getData() {
        return data;
    }

    public void setData(ArrayList<NearAdminDTO> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public NearAdminVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NearAdminVH(context, LayoutInflater.from(context).inflate(R.layout.item_near, parent, false), dataEvent);
    }

    @Override
    public void onBindViewHolder(NearAdminVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
