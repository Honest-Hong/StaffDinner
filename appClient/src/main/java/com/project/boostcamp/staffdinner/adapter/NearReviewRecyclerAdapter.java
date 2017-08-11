package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.domain.NearAdminDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.NearReviewVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class NearReviewRecyclerAdapter extends RecyclerView.Adapter<NearReviewVH> {
    private Context context;
    private ArrayList<ReviewDTO> data;
    private DataEvent<ReviewDTO> dataEvent;

    public NearReviewRecyclerAdapter(Context context, DataEvent<ReviewDTO> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
        this.data = new ArrayList<>();
    }

    public ArrayList<ReviewDTO> getData() {
        return data;
    }

    public void setData(ArrayList<ReviewDTO> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public NearReviewVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NearReviewVH(context, LayoutInflater.from(context).inflate(R.layout.item_review, parent, false), dataEvent);
    }

    @Override
    public void onBindViewHolder(NearReviewVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
