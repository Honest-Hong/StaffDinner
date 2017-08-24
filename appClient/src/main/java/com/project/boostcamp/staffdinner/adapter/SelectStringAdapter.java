package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.SelectStringVH;


/**
 * Created by Hong Tae Joon on 2017-08-17.
 * 문자열 선택 목록 어뎁터
 */

public class SelectStringAdapter extends RecyclerView.Adapter<SelectStringVH> {
    private final Context context;
    private String[] data;
    private final DataEvent<String> dataEvent;

    public SelectStringAdapter(Context context, DataEvent<String> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public SelectStringVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectStringVH(LayoutInflater.from(context).inflate(R.layout.item_select_string, parent, false), dataEvent);
    }

    @Override
    public void onBindViewHolder(SelectStringVH holder, int position) {
        holder.setupView(data[position]);
    }

    @Override
    public int getItemCount() {
        return data != null?
                data.length :
                0;
    }
}
