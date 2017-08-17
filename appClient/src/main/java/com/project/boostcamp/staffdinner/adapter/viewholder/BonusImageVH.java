package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-08-17.
 */

public class BonusImageVH extends BaseVH<String> {
    @BindView(R.id.image_view) ImageView imageView;
    private Context context;

    public BonusImageVH(View itemView, DataEvent<String> dataEvent, Context context) {
        super(itemView, dataEvent);
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BonusImageVH.this.dataEvent.onClick(data);
            }
        });
    }

    @Override
    public void setupView(String data) {
        this.data = data;
        GlideApp.with(context)
                .load(data)
                .centerCrop()
                .into(imageView);
    }
}
