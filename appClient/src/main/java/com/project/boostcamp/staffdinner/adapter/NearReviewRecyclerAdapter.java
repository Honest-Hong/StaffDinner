package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.EmptyVH;
import com.project.boostcamp.staffdinner.adapter.viewholder.ReviewVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-11
 * 근처 리뷰 목록 어뎁터.
 */

public class NearReviewRecyclerAdapter extends RecyclerView.Adapter<BaseVH> {
    private final Context context;
    private ArrayList<ReviewDTO> data;
    private final DataEvent<ReviewDTO> dataEvent;

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
        if(this.data == null) {
            this.data = new ArrayList<>();
        }
        if(this.data.size() == 0) {
            ReviewDTO dto = new ReviewDTO();
            dto.setViewType(-1);
            this.data.add(dto);
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {
            return new ReviewVH(LayoutInflater.from(context).inflate(R.layout.item_review, parent, false), dataEvent);
        } else {
            return EmptyVH.horizontal(parent, context.getString(R.string.not_exist_review));
        }
    }

    @Override
    public void onBindViewHolder(BaseVH holder, int position) {
        if(holder instanceof ReviewVH) {
            ((ReviewVH)holder).setupView(data.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
