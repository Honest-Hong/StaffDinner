package com.project.boostcamp.staffdinnerrestraurant.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.data.AdminEstimate;
import com.project.boostcamp.publiclibrary.data.DataEvent;
import com.project.boostcamp.publiclibrary.data.Estimate;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinnerrestraurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 */

public class EstimateVH extends BaseVH<AdminEstimate> {
    private Context context;
    @BindView(R.id.image_state) ImageView imageState;
    @BindView(R.id.text_title) TextView textTitle;
    @BindView(R.id.text_state) TextView textState;
    @BindView(R.id.text_writed_time) TextView textWritedTime;
    @BindView(R.id.text_message) TextView textMessage;

    public EstimateVH(View v, final DataEvent<AdminEstimate> dataEvent, Context context) {
        super(v, dataEvent);
        this.context = context;
        ButterKnife.bind(this, v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataEvent.onClick(data);
            }
        });
    }

    @Override
    public void setupView(AdminEstimate data) {
        this.data = data;
        setState(data.getState());
        textTitle.setText(context.getString(R.string.text_estimate_list_title, data.getClientName()));
        textWritedTime.setText(context.getString(R.string.text_estimate_list_writed_time, data.getWritedTime() + ""));
        textMessage.setText(data.getMessage());
    }

    private void setState(int state) {
        switch(state) {
            case Estimate.STATE_WATING:
                imageState.setImageResource(R.drawable.ic_error_orange_24dp);
                textState.setText(R.string.text_waiting);
                textState.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                break;
            case Estimate.STATE_CONTACTED:
                imageState.setImageResource(R.drawable.ic_check_circle_green_24dp);
                textState.setText(R.string.text_contacted);
                textState.setTextColor(ContextCompat.getColor(context, R.color.green));
                break;
            case Estimate.STATE_FAILED:
                imageState.setImageResource(R.drawable.ic_cancel_red_24dp);
                textState.setText(R.string.text_failed);
                textState.setTextColor(ContextCompat.getColor(context, R.color.red));
                break;
            case Estimate.STATE_CANCELED:
                imageState.setImageResource(R.drawable.ic_cancel_red_24dp);
                textState.setText(R.string.text_canceled);
                textState.setTextColor(ContextCompat.getColor(context, R.color.red));
                break;
        }
    }
}
