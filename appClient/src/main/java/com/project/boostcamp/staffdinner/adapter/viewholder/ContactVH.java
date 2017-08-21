package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 */

public class ContactVH extends BaseVH<ContactDTO> implements View.OnClickListener {
    private Context context;
    @BindView(R.id.frame_cloud) FrameLayout frameCloud;
    @BindView(R.id.text_client) TextView textClient;
    @BindView(R.id.text_admin) TextView textAdmin;
    @BindView(R.id.text_application_time) TextView textApplicationTime;
    @BindView(R.id.text_estimate_time) TextView textEstimateTime;
    @BindView(R.id.text_contact_time) TextView textContactTime;
    @BindView(R.id.text_past_time) TextView textPastTime;

    public ContactVH(View itemView, DataEvent<ContactDTO> dataEvent, Context context) {
        super(itemView, dataEvent);
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void setupView(ContactDTO data) {
        this.data = data;
        textClient.setText(context.getString(R.string.user_name, data.getClientName()));
        textAdmin.setText(data.getAdminName());
        String appTime = TimeHelper.getTimeString(data.getAppTime(), context.getString(R.string.default_time_pattern));
        textApplicationTime.setText(appTime);
        String estimateTime = TimeHelper.getTimeString(data.getEstimateTime(), context.getString(R.string.default_time_pattern));
        textEstimateTime.setText(estimateTime);
        String contactTime = TimeHelper.getTimeString(data.getContactTime(), context.getString(R.string.default_time_pattern));
        textContactTime.setText(contactTime);

        final int oneDay = 24 * 60 * 60 * 1000;
        long diff = System.currentTimeMillis() - data.getContactTime();
        if(diff < oneDay) {
            frameCloud.setVisibility(View.GONE);
        } else {
            frameCloud.setVisibility(View.VISIBLE);
        }

        textPastTime.setText(TimeHelper.getTimeDiffString(data.getContactTime()));
    }

    public void onClick(View view) {
        dataEvent.onClick(data);
    }
}
