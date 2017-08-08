package com.project.boostcamp.staffdinner.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.data.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 */

public class ContactVH extends BaseVH<ContactDTO> implements View.OnClickListener {
    private Context context;
    @BindView(R.id.text_title) TextView textTitle;
    @BindView(R.id.text_application_time) TextView textApplicationTime;
    @BindView(R.id.text_estimate_time) TextView textEstimateTime;
    @BindView(R.id.text_contact_time) TextView textContactTime;

    public ContactVH(View itemView, DataEvent<ContactDTO> dataEvent, Context context) {
        super(itemView, dataEvent);
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void setupView(ContactDTO data) {
        this.data = data;
        textTitle.setText(context.getString(R.string.text_contact_list_title, data.getClientName(), data.getAdminName()));
        String appTime = TimeHelper.getTimeString(data.getAppTime(), context.getString(R.string.default_time_pattern));
        textApplicationTime.setText(appTime);
        String estimateTime = TimeHelper.getTimeString(data.getEstimateTime(), context.getString(R.string.default_time_pattern));
        textEstimateTime.setText(estimateTime);
        String contactTime = TimeHelper.getTimeString(data.getContactTime(), context.getString(R.string.default_time_pattern));
        textContactTime.setText(contactTime);
    }

    public void onClick(View view) {
        dataEvent.onClick(data);
    }
}
