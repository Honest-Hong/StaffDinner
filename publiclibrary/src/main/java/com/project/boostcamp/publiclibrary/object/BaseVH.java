package com.project.boostcamp.publiclibrary.object;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.project.boostcamp.publiclibrary.inter.DataEvent;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 */

public abstract class BaseVH<T> extends RecyclerView.ViewHolder {
    protected T data;
    protected DataEvent<T> dataEvent;

    public BaseVH(View itemView, DataEvent<T> dataEvent) {
        super(itemView);
        this.dataEvent = dataEvent;
    }

    public abstract void setupView(T data);
}
