package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.domain.NearAdminDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.EmptyVH;
import com.project.boostcamp.staffdinner.adapter.viewholder.NearAdminVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 * 근처 식당 목록 어뎁터
 */

public class NearAdminRecyclerAdapter extends RecyclerView.Adapter<BaseVH> {
    private final Context context;
    private ArrayList<NearAdminDTO> data;
    private final DataEvent<NearAdminDTO> dataEvent;

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
        if(this.data.size() == 0) {
            NearAdminDTO dto = new NearAdminDTO();
            dto.setViewType(-1);
            this.data.add(dto);
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {
            return new NearAdminVH(context, LayoutInflater.from(context).inflate(R.layout.item_near, parent, false), dataEvent);
        } else {
            return EmptyVH.horizontal(parent, context.getString(R.string.not_exist_near_admin));
        }
    }

    @Override
    public void onBindViewHolder(BaseVH holder, int position) {
        if(holder instanceof NearAdminVH) {
            ((NearAdminVH)holder).setupView(data.get(position));
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
