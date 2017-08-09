package com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.data.AdminApplication;
import com.project.boostcamp.publiclibrary.data.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 */

public class ApplicationVH extends BaseVH<AdminApplication> {
    private Context context;
    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.text_message) TextView textMessage;
    @BindView(R.id.text_number) TextView textNumber;
    @BindView(R.id.text_time) TextView textTime;
    @BindView(R.id.text_distance) TextView textDistance;
    @BindView(R.id.button_detail) Button btnDetail;

    public ApplicationVH(View v, final DataEvent<AdminApplication> dataEvent, Context context) {
        super(v, dataEvent);
        this.context = context;
        ButterKnife.bind(this, v);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataEvent.onClick(data);
            }
        });
    }

    @Override
    public void setupView(AdminApplication data) {
        this.data = data;
        textName.setText(context.getString(R.string.text_application_name, data.getWriterName()));
        textMessage.setText(data.getTitle());
        textNumber.setText(context.getString(R.string.people_count, data.getNumber()));
        textTime.setText(TimeHelper.getTimeString(data.getTime(), context.getString(R.string.default_date)));
        textDistance.setText(context.getString(R.string.distance_kilo, data.getDistance()));
    }
}
