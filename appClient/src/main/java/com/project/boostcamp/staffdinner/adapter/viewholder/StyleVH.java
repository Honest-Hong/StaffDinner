package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-08-09.
 */

public class StyleVH extends BaseVH<Map.Entry<String, Boolean>> {
    CardView cardView;
    @BindView(R.id.text_view) TextView textView;

    public StyleVH(final View itemView, final DataEvent<Map.Entry<String, Boolean>> dataEvent) {
        super(itemView, dataEvent);
        ButterKnife.bind(this, itemView);
        cardView = (CardView) itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setValue(!data.getValue());
                cardView.setBackgroundColor(data.getValue()
                        ? ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary)
                        : ContextCompat.getColor(itemView.getContext(), R.color.white));
                dataEvent.onClick(data);
            }
        });
    }

    @Override
    public void setupView(Map.Entry<String, Boolean> data) {
        this.data = data;
        textView.setText(data.getKey());
        cardView.setBackgroundColor(data.getValue()
                ? ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary)
                : ContextCompat.getColor(itemView.getContext(), R.color.white));
    }
}
