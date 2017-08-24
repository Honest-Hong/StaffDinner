package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.BonusImageVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-17.
 * 식당의 추가 이미지 어뎁터
 */

public class BonusImageRecyclerAdapter extends RecyclerView.Adapter<BonusImageVH> {
    private final Context context;
    private ArrayList<String> data;
    private final DataEvent<String> dataEvent;

    public BonusImageRecyclerAdapter(Context context, DataEvent<String> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
        this.data = new ArrayList<>();
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public BonusImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BonusImageVH(LayoutInflater.from(context).inflate(R.layout.item_bonus_image, parent, false), dataEvent, context);
    }

    @Override
    public void onBindViewHolder(BonusImageVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
