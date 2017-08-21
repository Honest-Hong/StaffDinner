package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.data.StyleListData;
import com.project.boostcamp.publiclibrary.inter.ViewHolderEvent;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.StyleVH;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-09.
 */

public class StyleRecyclerAdapter extends RecyclerView.Adapter<StyleVH> {
    private Context context;
    private ArrayList<StyleListData> styles;
    private ViewHolderEvent<StyleListData> dataEvent;

    public StyleRecyclerAdapter(Context context, ViewHolderEvent<StyleListData> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
        this.styles = new ArrayList<>();
    }

    public void setStyles(ArrayList<StyleListData> styles) {
        this.styles = styles;
    }

    @Override
    public StyleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StyleVH(
                LayoutInflater.from(context).inflate(R.layout.item_style, parent, false),
                dataEvent
        );
    }

    @Override
    public void onBindViewHolder(StyleVH holder, int position) {
        holder.setupView(styles.get(position));
    }

    @Override
    public int getItemCount() {
        return styles.size();
    }
}
