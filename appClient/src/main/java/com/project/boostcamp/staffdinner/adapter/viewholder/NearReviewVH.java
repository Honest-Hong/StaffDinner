package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class NearReviewVH extends BaseVH<ReviewDTO> {
    private Context context;
    @BindView(R.id.text_writer) TextView textWriter;
    @BindView(R.id.text_receiver) TextView textReceiver;
    @BindView(R.id.text_content) TextView textContent;
    @BindView(R.id.text_time) TextView textTime;
    @BindView(R.id.rating_bar) MaterialRatingBar ratingBar;
    public NearReviewVH(Context context, View itemView, final DataEvent<ReviewDTO> dataEvent) {
        super(itemView, dataEvent);
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataEvent.onClick(data);
            }
        });
    }

    @Override
    public void setupView(ReviewDTO data) {
        this.data = data;
        textWriter.setText(data.getWriter());
        textReceiver.setText(data.getReceiver());
        textContent.setText(data.getContent());
        textTime.setText(TimeHelper.getTimeString(data.getWritedTime(), "MM/dd"));
        ratingBar.setProgress((int)(data.getRating()*2));
    }
}
