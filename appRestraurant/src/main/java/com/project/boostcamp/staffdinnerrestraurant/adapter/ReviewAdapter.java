package com.project.boostcamp.staffdinnerrestraurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder.ReviewVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewVH> {
    private Context context;
    private ArrayList<ReviewDTO> data;

    public ReviewAdapter(Context context, ArrayList<ReviewDTO> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ReviewVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewVH(LayoutInflater.from(context).inflate(R.layout.item_review, parent, false), null);
    }

    @Override
    public void onBindViewHolder(ReviewVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
