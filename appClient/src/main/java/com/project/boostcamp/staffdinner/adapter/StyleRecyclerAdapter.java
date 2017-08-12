package com.project.boostcamp.staffdinner.adapter;

import android.content.Context;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.viewholder.StyleVH;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hong Tae Joon on 2017-08-09.
 */

public class StyleRecyclerAdapter extends RecyclerView.Adapter<StyleVH> {
    private Context context;
    private ArraySet<Map.Entry<String, Boolean>> styles;
    private DataEvent<Map.Entry<String, Boolean>> dataEvent;

    public StyleRecyclerAdapter(Context context, DataEvent<Map.Entry<String, Boolean>> dataEvent) {
        this.context = context;
        this.dataEvent = dataEvent;
        this.styles = new ArraySet<>();
    }

    public void setStyles(ArraySet<Map.Entry<String, Boolean>> styles) {
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
        holder.setupView(styles.valueAt(position));
    }

    @Override
    public int getItemCount() {
        return styles.size();
    }
}
