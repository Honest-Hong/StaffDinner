package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ClientEstimateDTO;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;

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
    @BindView(R.id.button_detail) Button btnDetail;

    public EstimateVH(View v, DataEvent<ClientEstimateDTO> dataEvent, Context context) {
        super(v, dataEvent);
        this.context = context;
        ButterKnife.bind(this, v);
        btnDetail.setOnClickListener(this);
    }

    @Override
    public void setupView(ClientEstimateDTO data) {
        this.data = data;
        GlideApp.with(imageView.getContext())
                .load(RetrofitClient.getInstance().getAdminImageUrl(data.getAdmin().getId(), data.getAdmin().getType()))
                .centerCrop()
                .into(imageView);
        textName.setText(data.getAdmin().getName());
//        String strTime = TimeHelper.getTimeString(data.getWritedTime(), context.getString(R.string.default_time_pattern));
//        strTime += "(" + TimeHelper.getTimeDiffString(data.getWritedTime()) + ")";
        textDate.setText(TimeHelper.getTimeDiffString(data.getWritedTime()));
        textMessage.setText(data.getMessage());
    }

    @Override
    public void onClick(View view) {
        dataEvent.onClick(data);
    }
}
