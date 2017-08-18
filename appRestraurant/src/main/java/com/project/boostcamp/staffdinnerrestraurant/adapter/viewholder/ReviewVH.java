package com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Hong Tae Joon on 2017-08-18.
 */

public class ReviewVH extends BaseVH<ReviewDTO> {
    @BindView(R.id.text_receiver) TextView textTo;
    @BindView(R.id.text_writer) TextView textFrom;
    @BindView(R.id.text_content) TextView textContent;
    @BindView(R.id.text_time) TextView textTime;
    @BindView(R.id.rating_bar) MaterialRatingBar ratingBar;

    public ReviewVH(View itemView, DataEvent<ReviewDTO> dataEvent) {
        super(itemView, dataEvent);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setupView(ReviewDTO data) {
        this.data = data;
        textTo.setText(data.getReceiver());
        textFrom.setText(data.getWriter());
        textContent.setText(data.getContent());
        textTime.setText(TimeHelper.getTimeString(data.getWritedTime(), "MM/dd"));
        ratingBar.setProgress((int)(data.getRating()*2));
    }
}
