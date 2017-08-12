package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.data.ViewHolderData;
import com.project.boostcamp.publiclibrary.domain.ClientEstimateDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.ContactVH;
import com.project.boostcamp.staffdinner.adapter.viewholder.EmptyVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 */

public class ContactRecyclerAdapter extends RecyclerView.Adapter<BaseVH> {
    private Context context;
    private ArrayList<ContactDTO> data;
    private DataEvent<ContactDTO> dataEvent;

    public ContactRecyclerAdapter(Context context, DataEvent<ContactDTO> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
        data = new ArrayList<>();
    }

    public void setData(ArrayList<ContactDTO> data) {
        this.data = data;
        if(this.data.size() == 0) {
            ContactDTO dto = new ContactDTO();
            dto.setType(-1);
            this.data.add(dto);
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {
            return new ContactVH(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false), dataEvent, context);
        } else {
            return EmptyVH.vertical(parent, context.getString(R.string.not_exist_contact));
        }
    }

    @Override
    public void onBindViewHolder(BaseVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
