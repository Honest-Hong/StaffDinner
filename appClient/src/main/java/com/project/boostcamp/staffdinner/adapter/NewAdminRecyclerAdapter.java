package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.domain.NewAdminDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.EmptyVH;
import com.project.boostcamp.staffdinner.adapter.viewholder.NewAdminVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-12.
 */

public class NewAdminRecyclerAdapter extends RecyclerView.Adapter<BaseVH> {
    private Context context;
    private DataEvent<NewAdminDTO> dataEvent;
    private ArrayList<NewAdminDTO> data;

    public NewAdminRecyclerAdapter(Context context, DataEvent<NewAdminDTO> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
        this.data = new ArrayList<>();
    }

    public ArrayList<NewAdminDTO> getData() {
        return data;
    }

    public void setData(ArrayList<NewAdminDTO> data) {
        this.data = data;
    }

    @Override
    public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {
            return new NewAdminVH(LayoutInflater.from(context).inflate(R.layout.item_new_admin, parent, false), dataEvent);
        } else {
            return EmptyVH.horizontal(parent, context.getString(R.string.not_exist_new_admin));
        }
    }

    @Override
    public void onBindViewHolder(BaseVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getViewType();
    }
}
