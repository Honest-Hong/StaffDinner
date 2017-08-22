package com.project.boostcamp.staffdinnerrestraurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.data.ViewType;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder.ContactVH;
import com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder.LoadingVH;

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
        notifyDataSetChanged();
    }

    public ArrayList<ContactDTO> getData() {
        return data;
    }

    @Override
    public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ViewType.NORMAL) {
            return new ContactVH(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false), dataEvent, context);
        } else if(viewType == ViewType.LOADING) {
            return new LoadingVH(LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false), null);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseVH holder, int position) {
        if(holder instanceof ContactVH) {
            ((ContactVH)holder).setupView(data.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) == null
                ? ViewType.LOADING
                : ViewType.NORMAL;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
