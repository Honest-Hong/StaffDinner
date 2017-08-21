package com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.data.OSLData;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinnerrestraurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-08-18.
 */

public class OSLVH extends BaseVH<OSLData> {
    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.text_link) TextView textLink;
    @BindView(R.id.text_copyright) TextView textCopyright;
    @BindView(R.id.text_apache_license) TextView textApacheLicense;
    public OSLVH(View itemView, DataEvent<OSLData> dataEvent) {
        super(itemView, dataEvent);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setupView(OSLData data) {
        this.data = data;
        textName.setText(data.getName());
        textLink.setText(data.getLink());
        textCopyright.setText(data.getCopyright());
        textApacheLicense.setText(data.getApacheLicense());
    }
}
