package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.ContactVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 */

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactVH> {
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
        notifyDataSetChanged();
    }

    @Override
    public ContactVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactVH(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false), dataEvent, context);
    }

    @Override
    public void onBindViewHolder(ContactVH holder, int position) {
        holder.setupView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
