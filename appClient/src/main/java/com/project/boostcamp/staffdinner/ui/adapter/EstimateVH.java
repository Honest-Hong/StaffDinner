package com.project.boostcamp.staffdinner.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.data.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ClientEstimateDTO;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 */

public class EstimateVH extends BaseVH<ClientEstimateDTO> implements View.OnClickListener{
    private Context context;
    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.text_message) TextView textMessage;
    @BindView(R.id.text_date) TextView textDate;

    public EstimateVH(View v, DataEvent<ClientEstimateDTO> dataEvent, Context context) {
        super(v, dataEvent);
        this.context = context;
        ButterKnife.bind(this, v);
        v.setOnClickListener(this);
    }

    @Override
    public void setupView(ClientEstimateDTO data) {
        this.data = data;
        GlideApp.with(imageView.getContext())
                .load(data.getAdmin().getImage())
                .centerCrop()
                .into(imageView);
        textName.setText(data.getAdmin().getName());
        textDate.setText(TimeHelper.getTimeDiffString(data.getWritedTime()));
        textMessage.setText(StringHelper.cutEnd(data.getMessage(), 30));
    }

    @Override
    public void onClick(View view) {
        dataEvent.onClick(data);
    }
}
