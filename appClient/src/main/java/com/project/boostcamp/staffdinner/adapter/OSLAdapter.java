package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.data.OSLData;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.OSLVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-18.
 */

public class OSLAdapter extends RecyclerView.Adapter<OSLVH> {
    private Context context;
    private ArrayList<OSLData> data;

    public OSLAdapter(Context context, ArrayList<OSLData> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public OSLVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OSLVH(LayoutInflater.from(context).inflate(R.layout.item_osl, parent ,false), null);
    }

    @Override
    public void onBindViewHolder(OSLVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
