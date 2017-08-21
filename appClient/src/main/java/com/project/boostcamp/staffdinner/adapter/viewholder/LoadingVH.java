package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;

/**
 * Created by Hong Tae Joon on 2017-08-21.
 */

public class LoadingVH extends BaseVH<Void> {
    public LoadingVH(View itemView, DataEvent<Void> dataEvent) {
        super(itemView, dataEvent);
    }

    @Override
    public void setupView(Void data) {

    }
}
